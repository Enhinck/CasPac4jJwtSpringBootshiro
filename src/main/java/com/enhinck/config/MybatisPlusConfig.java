package com.enhinck.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;


// @Configuration
// @EnableConfigurationProperties(MybatisProperties.class)
// @MapperScan("com.enhinck.demo.mapper*")
public class MybatisPlusConfig {

	@Autowired
	Environment env;

	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Autowired(required = false)
	private Interceptor[] interceptors;

	//@Autowired(required = false)
	//private DatabaseIdProvider databaseIdProvider;

	/**
	 * mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType("mysql");
		// page.setOptimizeType(optimizeType);//分页count优化，需要的话请自行指定
		return page;
	}

	/**
	 * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
	 * <p>
	 * 配置文件和mybatis-boot的配置文件同步
	 *
	 * @return
	 */
	@Bean
	public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(DataSource dataSource) {
		MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
		mybatisPlus.setDataSource(dataSource);
		mybatisPlus.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.properties.getConfigLocation())) {
			mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
		}
		mybatisPlus.setConfiguration(properties.getConfiguration());
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			mybatisPlus.setPlugins(this.interceptors);
		}
		// MP 全局配置，更多内容进入类看注释
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbType(DBType.MYSQL.name());// 数据库类型
		// ID 策略 AUTO->`0`("数据库ID自增") INPUT->`1`(用户输入ID") ID_WORKER->`2`("全局唯一ID")
		// UUID->`3`("全局唯一ID")
		// globalConfig.setIdType(2);
		// MP 属性下划线 转 驼峰 , 如果原生配置 mc.setMapUnderscoreToCamelCase(true) 开启，该配置可以无。
		globalConfig.setDbColumnUnderline(true);
		mybatisPlus.setGlobalConfig(globalConfig);
		return mybatisPlus;
	}
}