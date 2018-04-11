package com.enhinck.shiro;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enhinck.demo.entity.UserInfo;
import com.enhinck.demo.repository.UserInfoRepository;

import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;

@Component("shiroPac4jRealm")
public class ShiroPac4jRealm extends Pac4jRealm{
	private static Logger logger = LoggerFactory.getLogger(ShiroPac4jRealm.class);
	@Autowired
	UserInfoRepository userInfoRepository;

	/**
	 * 单Cas服务登录校验通过后，便会调用这个方法，并携带用户信息的Token参数 假设只要是有Token过来，就说明是有效的登录用户，不再对密码等做校验
	 * 方法名称 : doGetAuthenticationInfo 功能描述 : 验证当前登陆的Subject
	 * 
	 * @param authcToken
	 *            当前登录用户的token
	 * @return 验证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
	/*	AuthenticationInfo pac4jToken = super.doGetAuthenticationInfo(authcToken);
		AuthenticationInfo token = super.doGetAuthenticationInfo(authcToken);
		//System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
		// 获取用户的输入的账号.
		
		
		String username = (String)token.getPrincipals().getPrimaryPrincipal();
		// String password = new String(usernamePasswordToken.getPassword());

		// 通过username从数据库中查找 User对象，如果找到，没找到.
		// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
		UserInfo userInfo = userInfoRepository.findByUsername(username);
		System.out.println("----->>userInfo=" + userInfo);
		if (userInfo == null) {
			return null;
		}
		
		final Pac4jToken token = (Pac4jToken) authcToken;  
        final LinkedHashMap<String, CommonProfile> profiles = token.getProfiles();  
        final Pac4jPrincipal principal = new Pac4jPrincipal(profiles);  
  
        String username = principal.getProfile().getId();  
        System.out.println("----->>username=" + username);
    	UserInfo userInfo = userInfoRepository.findByUsername(username);
		System.out.println("----->>userInfo=" + userInfo);
		if (userInfo == null) {
			return null;
		}
        Session session = SecurityUtils.getSubject().getSession();  
         
        session.setAttribute("userSessionId", username );  
         
  
        //return new SimpleAuthenticationInfo(userInfo, profiles.hashCode(), getName());  
		
        return pac4jToken;*/
        
        System.out.println("doGetAuthenticationInfo");
        
        final Pac4jToken token = (Pac4jToken) authenticationToken;
        final LinkedHashMap<String, CommonProfile> profiles = token.getProfiles();
        final Pac4jPrincipal principal = new Pac4jPrincipal(profiles);
        UserInfo userInfo = null;
        
       String username = principal.getProfile().getId();
       userInfo = userInfoRepository.findByUsername(username);
        for (Entry<String, CommonProfile> entry : profiles.entrySet()) {
            String clientName = entry.getKey();
            logger.info("principal.getName():{}",username);
            CommonProfile profile = entry.getValue();
            Map<String, Object> attributeMap = profile.getAttributes();
            logger.info("attributeMap:{}",attributeMap);
        }
        logger.info("userInfo:{}",userInfo);
  //     return super.doGetAuthenticationInfo(authenticationToken);
        return new SimpleAuthenticationInfo(userInfo, profiles.hashCode(), getName());
	}

	/**
	 * 这里应该是请求用户的权限的方法，页面中 <shiro:hasRole name="ROLE_ADMIN">
	 * 等类似的权限标签才会请求的方法，迁移过来业务相关代码，不解释了. 方法名称 : doGetAuthorizationInfo 功能描述 :
	 * 获取登录用户的权限信息
	 * 
	 * @param principals
	 *            登录用户信息
	 * @return 用户权限信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		  SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		System.out.println("doGetAuthorizationInfo");
		
		UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
		logger.info(userInfo.getName());
		return authorizationInfo;
	}
}
