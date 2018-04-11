package com.enhinck;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
// @ServletComponentScan//扫描方式添加Servlet、Filter、ServletListener
@EnableMongoRepositories
// @ImportResource(locations={"classpath:spring-shiro-cas.xml"})
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

	@Bean
	public Docket buildDocket() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(clientName());
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder().title("DemoAPI文档").termsOfServiceUrl("http://enhinck.site")
						.description("DemoAPI文档")
						.contact(new Contact("enhinck", "http://enhinck.site", "huenbin@foxmail.com")).build())
				.select().apis(RequestHandlerSelectors.basePackage("com.enhinck.demo.api"))// controller路径
				.paths(PathSelectors.any()).build().globalOperationParameters(parameters);
		// return new Docket(DocumentationType.SWAGGER_2);
	}

	private Parameter clientName() {
		return new ParameterBuilder().name("client_name").description("").modelRef(new ModelRef("string"))
				.parameterType("query").defaultValue("rest").required(false).build();
	}

	// 自注册servlet
	/*
	 * @Bean public ServletRegistrationBean testServletRegistration() {
	 * ServletRegistrationBean registration = new ServletRegistrationBean(new
	 * DruidStatViewServlet()); registration.getUrlMappings().clear();
	 * registration.addUrlMappings("*.do"); registration.addUrlMappings("*.json");
	 * return registration; }
	 */

	/*
	 * @Bean public FilterRegistrationBean testFilterRegistration() {
	 * FilterRegistrationBean registration = new FilterRegistrationBean(new
	 * DruidStatFilter()); registration.getUrlPatterns().clear();
	 * registration.getUrlPatterns().add("/*"); return registration; }
	 */

	/*
	 * @Bean public ServletListenerRegistrationBean
	 * servletListenerRegistrationBean(){ ServletListenerRegistrationBean
	 * servletListenerRegistrationBean = new ServletListenerRegistrationBean();
	 * servletListenerRegistrationBean.setListener(new IndexListener()); return
	 * servletListenerRegistrationBean; }
	 */

}
