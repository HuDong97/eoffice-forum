package com.eoffice.userBehavior.config;

import com.eoffice.model.article.pojos.Article;
import com.eoffice.model.userBehavior.comments.vo.Comments;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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


import java.util.List;
import java.util.Map;

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

    @Bean(name = "userBehaviorRedisConnectionFactory")
    public RedisConnectionFactory userBehaviorRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(1); // 用户行为（点赞收藏评论数）存储在数据库1
        return new LettuceConnectionFactory(config);
    }


    @Bean(name = "commentRedisConnectionFactory")
    public RedisConnectionFactory commentRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(3); // 评论内容存储在数据库3
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


    @Bean(name = "userBehaviorRedisTemplate")
    public RedisTemplate<String, Map<String, Integer>> userBehaviorRedisTemplate(@Qualifier("userBehaviorRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        // 修改此处的参数，加上@Qualifier注解，指定使用userBehaviorRedisConnectionFactory
        RedisTemplate<String, Map<String, Integer>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Map.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Map.class));
        return template;
    }

    @Bean(name = "commentListRedisTemplate")
    public RedisTemplate<String, List<Comments>> commentListRedisTemplate(
            @Qualifier("commentRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, List<Comments>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        // 配置Jackson2JsonRedisSerializer
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())  // 注册JavaTimeModule以支持Java 8的日期和时间类型
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用将日期序列化为时间戳

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化List<Comments>
        JavaType commentsListType = objectMapper.getTypeFactory().constructCollectionType(List.class, Comments.class);
        Jackson2JsonRedisSerializer<List<Comments>> serializer = new Jackson2JsonRedisSerializer<>(commentsListType);

        // 设置template的序列化器
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }





}
