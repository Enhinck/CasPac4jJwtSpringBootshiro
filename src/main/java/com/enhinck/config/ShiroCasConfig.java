/*package com.enhinck.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.enhinck.redis.RedisCacheManager;
import com.enhinck.redis.RedisSessionDAO;

//@Configuration
public class ShiroCasConfig {
	private static Logger logger = LoggerFactory.getLogger(ShiroCasConfig.class);
	// 路径不能改
	private static final String casFilterUrlPattern = "/shiro-cas";

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean("redisCacheManager")
	public RedisCacheManager redisCacheManager() {
		return new RedisCacheManager();
	}

	@Bean("sessionManager")
	public SessionManager sessionManager(RedisSessionDAO sessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setGlobalSessionTimeout(1800);
		sessionManager.setCacheManager(redisCacheManager());
		return sessionManager;
	}
	
	*//**
	 * 不指定名字的话，自动创建一个方法名第一个字母小写的bean
	 *//*
	@Bean("securityManager")
	@DependsOn({ "redisManager", "sessionManager" })
	public SecurityManager securityManager(CasRealm shiroRealm, RedisCacheManager redisManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(shiroRealm);
		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(redisManager);
		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager);
		securityManager.setSubjectFactory(new CasSubjectFactory());
		//securityManager.setAuthenticator(authenticator);
		return securityManager;
	}

	// 按你业务修改
	// anon表示不过滤
	// casFilter自定义过滤器:验证成功跳转地址/验证失败跳转地址
	// logout:自定义过滤器:过滤单点登录退出请求
	private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 你需要CAS校验的请求
		filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");
		filterChainDefinitionMap.put("/static/**", "anon");
		// 单点登录退出请求拦截
		filterChainDefinitionMap.put("/logout", "logout");
		// 不过滤其他业务系统请求
		filterChainDefinitionMap.put("/templates/*", "anon");
		filterChainDefinitionMap.put("/**", "authc");// 其他地址拦截
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		// shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		// shiroFilterFactoryBean.setSuccessUrl("/index");

		// 未授权界面;
		// shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, CasFilter casFilter,
			@Value("${shiro.cas}") String casServerUrlPrefix, @Value("${shiro.server}") String shiroServerUrlPrefix) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		String loginUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		shiroFilterFactoryBean.setSuccessUrl("/");
		Map<String, Filter> filters = new HashMap<>();
		filters.put("casFilter", casFilter);
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl(casServerUrlPrefix + "/logout?service=" + shiroServerUrlPrefix);
		filters.put("logout", logoutFilter);
		shiroFilterFactoryBean.setFilters(filters);
		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}

	*//**
	 * 定义 CAS Filter
	 *//*
	@Bean(name = "casFilter")
	public CasFilter casFilter(@Value("${shiro.cas}") String casServerUrlPrefix,
			@Value("${shiro.server}") String shiroServerUrlPrefix) {
		CasFilter casFilter = new CasFilter();
		casFilter.setName("casFilter");
		casFilter.setEnabled(true);
		// 校验失败地址，这里失败继续重定向单点登录界面
		String failUrl = casServerUrlPrefix + "/login?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
		// 校验成功地址，登录成功后重定向的地址
		String successUrl = shiroServerUrlPrefix + "/";
		casFilter.setFailureUrl(failUrl);
		casFilter.setSuccessUrl(successUrl);
		return casFilter;
	}

}
*/