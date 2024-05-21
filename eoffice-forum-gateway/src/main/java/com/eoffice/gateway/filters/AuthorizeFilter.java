package com.eoffice.gateway.filters;

import com.eoffice.utils.common.JwtUtil;
import com.eoffice.utils.thread.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {

    private final StringRedisTemplate stringRedisTemplate;

    public AuthorizeFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request和response对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //判断是否是登录
        if (request.getURI().getPath().contains("/user/login") || request.getURI().getPath().contains("/user/register")) {
            //放行
            return chain.filter(exchange);
        }

        // 从请求头中获取 Authorization 参数
        String token = request.getHeaders().getFirst("Authorization");
        // 验证 token，查看是否持有有效的 JWT 令牌
        try {
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("缓存失效，请重新登录");
            }

            //从redis中获取相同的token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if (redisToken == null) {
                //token已经失效了
                throw new RuntimeException("密码已修改，请重新登录");
            }

            Map<String, Object> claims = JwtUtil.parseToken(token);

            //把业务数据存储到ThreadLocalUtil中
            ThreadLocalUtil.setUser(claims);

            // 如果验证通过，放行请求
            return chain.filter(exchange);
        } catch (Exception e) {
            // 如果验证失败，设置响应状态码为 401，并返回 false，表示拦截该请求
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
    }

    /**
     * 优先级设置  值越小  优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
