package com.eoffice.user.config;


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


import java.util.Map;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    // 配置 0 号数据库用于存储 token
    @Bean(name = "tokenRedisConnectionFactory")
    public RedisConnectionFactory tokenRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(0); // token存储在数据库0
        return new LettuceConnectionFactory(config);
    }

    // 配置 4 号数据库用于存储邮箱和验证码
    @Bean(name = "emailAndVerificationCodeRedisConnectionFactory")
    public RedisConnectionFactory emailAndVerificationCodeRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(4); // 忘记密码页面的邮箱和验证码
        return new LettuceConnectionFactory(config);
    }



    // 用于 token 的 RedisTemplate
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

    // 用于邮箱和验证码的 RedisTemplate
    @Bean(name = "emailRedisTemplate")
    public StringRedisTemplate emailRedisTemplate(@Qualifier("emailAndVerificationCodeRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }


}
