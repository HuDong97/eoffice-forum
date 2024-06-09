package com.eoffice.article.config;

import com.eoffice.model.article.pojos.Article;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean(name = "tokenRedisConnectionFactory")
    public RedisConnectionFactory tokenRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(0); // token存储在数据库0
        return new LettuceConnectionFactory(config);
    }

    @Bean(name = "articleRedisConnectionFactory")
    public RedisConnectionFactory articleRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(2); // 文章存储在数据库2
        return new LettuceConnectionFactory(config);
    }

    @Bean(name = "redisTemplate")
    public StringRedisTemplate redisTemplate(@Qualifier("tokenRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        // 在方法参数上添加@Qualifier注解，指定使用tokenRedisConnectionFactory
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean(name = "articleRedisTemplate")
    public RedisTemplate<String, Article> articleRedisTemplate(@Qualifier("articleRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Article> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        // 配置Jackson2JsonRedisSerializer
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        Jackson2JsonRedisSerializer<Article> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Article.class);

        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        return template;
    }

}
