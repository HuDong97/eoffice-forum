package com.eoffice.userBehavior.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Map<String, Integer>> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Map<String, Integer>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用String序列化键
        template.setKeySerializer(new StringRedisSerializer());

        // 使用Jackson序列化值
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
