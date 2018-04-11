/*package com.enhinck.shiro;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.enhinck.demo.entity.UserInfo;
import com.enhinck.demo.repository.UserInfoRepository;

//@Component("casRealm")
public class MyRealm extends CasRealm {
	   //路径不能改
    private static final String casFilterUrlPattern = "/shiro-cas";
	private static Logger logger = LoggerFactory.getLogger(MyRealm.class);
	@Value("${shiro.cas}")
	String casServerUrlPrefix;
	@Value("${shiro.server}")
	String shiroServerUrlPrefix;

	@Autowired
	UserInfoRepository userInfoRepository;

	@PostConstruct
	public void intCas() {
		setCasServerUrlPrefix(casServerUrlPrefix);
		setCasService(shiroServerUrlPrefix + casFilterUrlPattern);
		logger.info("CasServerUrlPrefix:{},CasService{}", casServerUrlPrefix,
				shiroServerUrlPrefix + casFilterUrlPattern);
	}

	*//**
	 * 单Cas服务登录校验通过后，便会调用这个方法，并携带用户信息的Token参数 假设只要是有Token过来，就说明是有效的登录用户，不再对密码等做校验
	 * 方法名称 : doGetAuthenticationInfo 功能描述 : 验证当前登陆的Subject
	 * 
	 * @param authcToken
	 *            当前登录用户的token
	 * @return 验证信息
	 *//*
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		AuthenticationInfo token = super.doGetAuthenticationInfo(authcToken);
		// System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
		// 获取用户的输入的账号.

		String username = (String) token.getPrincipals().getPrimaryPrincipal();
		// String password = new String(usernamePasswordToken.getPassword());

		// 通过username从数据库中查找 User对象，如果找到，没找到.
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		System.out.println("----->>userInfo=" + userInfo);
		if (userInfo == null) {
			return null;
		}

		return token;
	}

	*//**
	 * 这里应该是请求用户的权限的方法，页面中 <shiro:hasRole name="ROLE_ADMIN">
	 * 等类似的权限标签才会请求的方法，迁移过来业务相关代码，不解释了. 方法名称 : doGetAuthorizationInfo 功能描述 :
	 * 获取登录用户的权限信息
	 * 
	 * @param principals
	 *            登录用户信息
	 * @return 用户权限信息
	 *//*
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		System.out.println("doGetAuthorizationInfo");
		return authorizationInfo;
	}

}
*/