package com.eoffice.article.interceptors;


import com.eoffice.utils.common.JwtUtil;
import com.eoffice.utils.thread.ThreadLocalUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public TokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 在请求处理之前进行拦截
        // 从请求头中获取 Authorization 参数
        String token = request.getHeader("Authorization");
        // 验证 token，查看是否持有有效的 JWT 令牌
        try {
            //从redis中获取相同的token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if (redisToken==null){
                //token已经失效了
                throw new RuntimeException();

            }
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //把业务数据存储到ThreadLocalUtil中
            ThreadLocalUtil.set(claims);
            // 如果验证通过，放行请求
            return true;
        } catch (Exception e) {
            // 如果验证失败，设置响应状态码为 401，并返回 false，表示拦截该请求
            response.setStatus(401);
            return false;
        }

    }


    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理之后但视图渲染之前执行
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        // 在请求处理完全完成后执行，通常用于资源清理工作
        ThreadLocalUtil.remove();

    }
}



