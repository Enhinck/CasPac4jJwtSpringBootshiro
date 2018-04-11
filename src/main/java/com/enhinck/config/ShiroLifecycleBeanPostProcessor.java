package com.enhinck.config;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  Shiro生命周期处理器
 * lifecycleBeanPostProcessor 必须单独配置 不然配置此类的@Value都无法获取值
 * @author huenbin
 * @date 2018年4月10日
 */
@Configuration
public class ShiroLifecycleBeanPostProcessor {
	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
}
