package com.enhinck.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.enhinck.redis.RedisObjectSerializer;

/**
 * Created by Enhinck on 2017/8/23
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Bean
	public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(1800);
		return cacheManager;
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new RedisObjectSerializer());
		return template;
	}

	/*
	 * @Bean public KeyGenerator keyGenerator() { return new KeyGenerator() {
	 * 
	 * @Override public Object generate(Object target, Method method, Object...
	 * params) { StringBuilder sb = new StringBuilder();
	 * sb.append(target.getClass().getName()); sb.append(method.getName()); for
	 * (Object obj : params) { sb.append(obj.toString()); } return sb.toString(); }
	 * };//earlary JDK
	 * 
	 * return (target, method, params) -> { StringBuilder sb = new StringBuilder();
	 * sb.append(target.getClass().getName()); sb.append(method.getName()); for
	 * (Object obj : params) { sb.append(obj.toString()); } return sb.toString(); };
	 * }
	 * 
	 * @Bean public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
	 * RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
	 * //设置缓存过期时间 redisCacheManager.setDefaultExpiration(3600);//秒 return
	 * redisCacheManager; }
	 * 
	 * @SuppressWarnings({ "unchecked", "rawtypes" })
	 * 
	 * @Bean public RedisTemplate<String, String>
	 * redisTemplate(RedisConnectionFactory factory) { StringRedisTemplate template
	 * = new StringRedisTemplate(factory); Jackson2JsonRedisSerializer
	 * jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
	 * ObjectMapper om = new ObjectMapper(); om.setVisibility(PropertyAccessor.ALL,
	 * JsonAutoDetect.Visibility.ANY);
	 * om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	 * jackson2JsonRedisSerializer.setObjectMapper(om);
	 * template.setValueSerializer(jackson2JsonRedisSerializer);
	 * template.afterPropertiesSet(); return template; }
	 */
}