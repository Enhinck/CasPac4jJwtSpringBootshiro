package com.enhinck.demo.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.profile.CasProfile;
import org.pac4j.cas.profile.CasRestProfile;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
@RequestMapping("/")
public class LoginController extends BaseAPI {
	@Resource
	CasRestFormClient casRestFormClient;
	@Resource
	JwtGenerator generator;
	@Value("${cas.serviceUrl}") 
	String serviceUrl;
	
	@ApiOperation("登录接口")
	@RequestMapping("/user/login")
	public Object login(String username,String password,HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> model = new HashMap<>();
	    J2EContext context = new J2EContext(request, response);
	    final ProfileManager<CasRestProfile> manager = new ProfileManager(context);
	    final Optional<CasRestProfile> profile = manager.get(true);
	    //获取ticket
	    TokenCredentials tokenCredentials = casRestFormClient.requestServiceTicket(serviceUrl, profile.get(), context);
	    //根据ticket获取用户信息
	    final CasProfile casProfile = casRestFormClient.validateServiceTicket(serviceUrl, tokenCredentials, context);
	    //生成jwt token
	    String token = generator.generate(casProfile);
	    model.put("token", token);
	    return new HttpEntity<>(model);
	}
	
	
	@PostMapping("/login")
	public String  login(HttpServletRequest request, Map<String, Object> map) throws Exception{
	    System.out.println("HomeController.login()");
	    // 登录失败从request中获取shiro处理的异常信息。
	    // shiroLoginFailure:就是shiro异常类的全类名.
	    
	   Object object =  request.getAttribute("shiroLoginFailure");
	    System.out.println(object);
	    String exception = (String) request.getAttribute("shiroLoginFailure");
	    System.out.println("exception=" + exception);
	    String msg = "";
	    if (exception != null) {
	        if (UnknownAccountException.class.getName().equals(exception)) {
	            System.out.println("UnknownAccountException -- > 账号不存在：");
	            msg = "UnknownAccountException -- > 账号不存在：";
	        } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
	            System.out.println("IncorrectCredentialsException -- > 密码不正确：");
	            msg = "IncorrectCredentialsException -- > 密码不正确：";
	        } else if ("kaptchaValidateFailed".equals(exception)) {
	            System.out.println("kaptchaValidateFailed -- > 验证码错误");
	            msg = "kaptchaValidateFailed -- > 验证码错误";
	        } else {
	            msg = "else >> "+exception;
	            System.out.println("else -- >" + exception);
	        }
	    }
	    map.put("msg", msg);
	    //model.addAttribute("msg", msg);
	    // 此方法不处理登录成功,由shiro进行处理
	    return "login";
	}
	
	@GetMapping("/login")
	public String loginPage(HttpServletRequest request) throws Exception{
		String msg = request.getParameter("msg");
		System.out.println(msg);
	    return "login";
	}
	@GetMapping("/index")
	public String index() throws Exception{
	   
	    return "index";
	}
	

}
