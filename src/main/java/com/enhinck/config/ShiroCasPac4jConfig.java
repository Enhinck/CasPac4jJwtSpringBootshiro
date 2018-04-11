package com.enhinck.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
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
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jSubjectFactory;

//@Configuration
public class ShiroCasPac4jConfig {
	private static Logger logger = LoggerFactory.getLogger(ShiroCasPac4jConfig.class);
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
	
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, Config config) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		//String loginUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		//shiroFilterFactoryBean.setSuccessUrl("/");
		
		// shiroFilterFactoryBean.setSuccessUrl("/view/hello");
		Map<String, Filter> filters = new HashMap<>();
		filters.put("callbackFilter", callbackFilter(config));
		filters.put("casSecurityFilter", casSecurityFilter(config));
		
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl(logoutUrl);
		filters.put("logout", logoutFilter);
		shiroFilterFactoryBean.setFilters(filters);
		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}

	private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 你需要CAS校验的请求
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/v2/**", "anon");
		filterChainDefinitionMap.put("/**/api-docs", "anon");
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		
		
		
		
	
		// 单点登录退出请求拦截
		filterChainDefinitionMap.put("/logout", "logout");
		// 不过滤其他业务系统请求
		filterChainDefinitionMap.put("/templates/*", "anon");
		filterChainDefinitionMap.put("/callback", "callbackFilter");
		filterChainDefinitionMap.put("/SecurityLogin", "casSecurityFilter");
		filterChainDefinitionMap.put("/**", "casSecurityFilter");
		
		shiroFilterFactoryBean.setLoginUrl("/SecurityLogin");//特殊地址

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
		return casClient;
	}

	@Bean
	public CasConfiguration casConfiguration() {
		CasConfiguration casConfiguration = new CasConfiguration();
		casConfiguration.setLoginUrl(loginUrl);
		return casConfiguration;
	}

	@Bean
	public Clients clients(CasClient casClient) {
		Clients clients = new Clients();
		clients.setCallbackUrl(callbackUrl);
		clients.setClients(casClient);
		return clients;
	}

	@Bean
	public Config config(Clients clients) {
		Config config = new Config();
		config.setClients(clients);
		return config;
	}

	//禁止springboot自己在注入
	public SecurityFilter casSecurityFilter(Config config) {
		SecurityFilter securityFilter = new SecurityFilter();
		securityFilter.setConfig(config);
		securityFilter.setClients("CasClient");
		return securityFilter;
	}
	
	//禁止springboot自己在注入
	public CallbackFilter callbackFilter(Config config) {
		CallbackFilter callbackFilter = new CallbackFilter();
		callbackFilter.setConfig(config);
		callbackFilter.setMultiProfile(true);
		
		//callbackFilter.setDefaultUrl("/view/hello");
		return callbackFilter;
	}


}
