package com.wangjf.mail.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 * 为了实习redis序列化
 */
@Configuration
public class RedisMailConfig {

    // TODO: 2022/11/18 这边bean生成有bug！ 
    @Bean
    public RedisTemplate<String, Object> redisMailTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisMailTemplate = new RedisTemplate<>();
        // String类型 key序列器
        redisMailTemplate.setKeySerializer(new StringRedisSerializer());
        // String类型 value序列器
        redisMailTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash类型 key序列器
        redisMailTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Hash类型 value序列器
        redisMailTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisMailTemplate.setConnectionFactory(redisConnectionFactory);
        return redisMailTemplate;
    }
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        StringRedisTemplate redisTemplate = new StringRedisTemplate();
////        // String类型 key序列器
////        redisTemplate.setKeySerializer(new StringRedisSerializer());
////        // String类型 value序列器
////        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
////        // Hash类型 key序列器
////        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
////        // Hash类型 value序列器
////        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        return redisTemplate;
//    }
}
