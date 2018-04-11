package com.enhinck.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = {"com.enhinck.demo.api", "com.enhinck.demo.repository","com.enhinck.druid","com.enhinck.redis"})
//@Import(value = {MybatisPlusConfig.class})
public class SpringConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
          registry.addResourceHandler("/webjars/**")
                 .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}
