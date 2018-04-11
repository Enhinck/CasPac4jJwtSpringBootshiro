package com.enhinck.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.enhinck.demo.entity.UserInfo;
import com.enhinck.demo.repository.UserInfoRepository;

public class MyShiroRealm extends AuthorizingRealm {

	@Autowired
	UserInfoRepository userInfoRepository;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		// TODO Auto-generated method stub
		return true;
	}

	

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
	    //获取用户的输入的账号.
	   
	    UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
	    
	    System.out.println(token.getCredentials());
	    String username = usernamePasswordToken.getUsername();
		   
	    //String password = new String(usernamePasswordToken.getPassword());
		   
	    //通过username从数据库中查找 User对象，如果找到，没找到.
	    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
	    UserInfo userInfo = userInfoRepository.findByUsername(username);
	    System.out.println("----->>userInfo="+userInfo);
	    if(userInfo == null){
	        return null;
	    }
	    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
	            userInfo, //用户名
	            userInfo.getPassword(), //密码
	            ByteSource.Util.bytes(userInfo.getSalt()),//salt=username+salt
	            userInfo.getName()  //realm name
	    );
	    
	    return authenticationInfo;
	}

}
