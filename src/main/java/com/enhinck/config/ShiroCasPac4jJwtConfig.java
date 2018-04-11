package com.enhinck.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.config.CasProtocol;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.enhinck.redis.RedisCacheManager;
import com.enhinck.redis.RedisSessionDAO;

import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jSubjectFactory;

@Configuration
public class ShiroCasPac4jJwtConfig extends AbstractShiroWebFilterConfiguration{
	private static Logger logger = LoggerFactory.getLogger(ShiroCasPac4jJwtConfig.class);
	@Value("${cas.callback.path}")
	String callbackUrl;
	@Value("${cas.login.path}")
	String loginUrl;
	@Value("${cas.logout.path}")
	String logoutUrl;
	@Value("${pac4j.clientName}") 
	String clientName;
	@Value("${cas.serviceUrl}") 
	String shiroServerUrlPrefix;
    @Value("${cas.prefixUrl}")
	String prefixUrl;
	@Value("${cas.loginUrl}")
	private String casLoginUrl;
	//@Value("#{ @environment['cas.callbackUrl'] ?: null }")
	//private String callbackUrl;
	 
	
    //jwt秘钥
    @Value("${jwt.salt}")
    private String salt;
	
	
	@Bean("redisCacheManager")
	public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager();
	}
	
	/**
	 * 不指定名字的话，自动创建一个方法名第一个字母小写的bean
	 */
	@Bean("securityManager")
	@DependsOn({ "redisCacheManager", "sessionManager" })
	public SecurityManager securityManager(Pac4jRealm pac4jRealm, RedisCacheManager redisManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(pac4jRealm);
		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(redisManager);
		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager);
		securityManager.setSubjectFactory(pac4jSubjectFactory());
		// securityManager.setAuthenticator(authenticator);
		return securityManager;
	}
	
	 /**
     * 路径过滤设置
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
     /*   definition.addPathDefinition("/callback", "callbackFilter");
        definition.addPathDefinition("/logout", "logoutFilter");
        definition.addPathDefinition("/**", "casSecurityFilter");*/
        return definition;
    }
	
	
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, Config config) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = super.shiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		shiroFilterFactoryBean.setFilters(filters(config));
		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}
	
	 /**
     * 对shiro的过滤策略进行明确
     * @return
     */
    protected Map<String, Filter> filters(Config config) {
        //过滤器设置
        Map<String, Filter> filters = new HashMap<>();
        filters.put("casSecurityFilter", casSecurityFilter(config));
        filters.put("callbackFilter", callbackFilter(config));
        LogoutFilter  logoutFilter = new LogoutFilter();
		logoutFilter.setConfig(config);
        filters.put("logoutFilter", logoutFilter);
        return filters;
    }

	private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 不过滤的请求
		filterChainDefinitionMap.put("/static/**", "anon");
		//filterChainDefinitionMap.put("/user/login", "anon");
		filterChainDefinitionMap.put("/**/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/**/webjars/**", "anon");
		filterChainDefinitionMap.put("/**/v2/**", "anon");
		
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		// 单点登录退出请求拦截
		filterChainDefinitionMap.put("/logout", "logoutFilter");
		// 回调
		filterChainDefinitionMap.put("/callback", "callbackFilter");
		// 安全地址
		//filterChainDefinitionMap.put("/SecurityLogin", "casSecurityFilter");
		filterChainDefinitionMap.put("/**", "casSecurityFilter");
		
		//filterChainDefinitionMap.put("/**", "authc");
		//shiroFilterFactoryBean.setLoginUrl("/SecurityLogin");//特殊地址
		//shiroFilterFactoryBean.setLoginUrl("/user/login");//特殊地址

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}


	@Bean("sessionManager")
	public SessionManager sessionManager(RedisSessionDAO sessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setGlobalSessionTimeout(1800);
		sessionManager.setCacheManager(redisCacheManager());
		return sessionManager;
	}

	public Pac4jSubjectFactory pac4jSubjectFactory() {
		return new Pac4jSubjectFactory();
	}


	@Bean
	public CasClient casClient(CasConfiguration casConfiguration) {
		CasClient casClient = new CasClient();
		casClient.setConfiguration(casConfiguration);
		casClient.setName(clientName);  
		casClient.setCallbackUrl(callbackUrl);
		return casClient;
	}

	@Bean
	public CasConfiguration casConfiguration() {
		CasConfiguration casConfiguration = new CasConfiguration(loginUrl);
		//casConfiguration.setLoginUrl();
		casConfiguration.setProtocol(CasProtocol.CAS30);
	    casConfiguration.setPrefixUrl(prefixUrl);
		return casConfiguration;
	}

	@Bean
	public Clients clients(CasClient casClient,CasConfiguration casConfiguration) {
		Clients clients = new Clients();
		//clients.setCallbackUrl(callbackUrl);
		
		//token校验器，可以用HeaderClient更安全
        ParameterClient parameterClient = new ParameterClient("token", jwtAuthenticator());
        parameterClient.setSupportGetRequest(true);
        parameterClient.setName("jwt");
        //支持的client全部设置进去
        clients.setClients(casClient, casRestFormClient(casConfiguration), parameterClient);
		
		//clients.setClients(casClient);
		return clients;
	}
	
	 /**
     * JWT校验器，也就是目前设置的ParameterClient进行的校验器，是rest/或者前后端分离的核心校验器
     * @return
     */
    @Bean
    protected JwtAuthenticator jwtAuthenticator() {
        JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
        jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration(salt));
        jwtAuthenticator.addEncryptionConfiguration(new SecretEncryptionConfiguration(salt));
        return jwtAuthenticator;
    }

	@Bean
	public Config config(Clients clients) {
		Config config = new Config();
		config.setClients(clients);
		return config;
	}
	
	 /**
     * JWT Token 生成器，对CommonProfile生成然后每次携带token访问
     * @return
     */
    @Bean
    protected JwtGenerator jwtGenerator() {
        return new JwtGenerator(new SecretSignatureConfiguration(salt), new SecretEncryptionConfiguration(salt));
    }
	
    /**
     * 通过rest接口可以获取tgt，获取service ticket，甚至可以获取CasProfile
     * @return
     */
    @Bean
    protected CasRestFormClient casRestFormClient(CasConfiguration casConfiguration) {
        CasRestFormClient casRestFormClient = new CasRestFormClient();
        casRestFormClient.setConfiguration(casConfiguration);
        casRestFormClient.setName("rest");
        return casRestFormClient;
    }
    

	//禁止springboot自己在注入
	public SecurityFilter casSecurityFilter(Config config) {
		SecurityFilter securityFilter = new SecurityFilter();
		securityFilter.setConfig(config);
		//securityFilter.setClients("CasClient");
		securityFilter.setClients("CasClient,rest,jwt");
		
		return securityFilter;
	}
	
	//禁止springboot自己在注入
	public CallbackFilter callbackFilter(Config config) {
		CallbackFilter callbackFilter = new CallbackFilter();
		callbackFilter.setConfig(config);
		//callbackFilter.setMultiProfile(true);
		
		//callbackFilter.setDefaultUrl("/view/hello");
		return callbackFilter;
	}


}
