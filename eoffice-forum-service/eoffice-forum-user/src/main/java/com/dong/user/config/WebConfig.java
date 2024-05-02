package com.dong.user.config;


import com.dong.user.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login", "/user/register");

       /* registry.addInterceptor(loginInterceptor)
                //设置拦截的路径
                .addPathPatterns("/secured/**")
                //设置排除的路径登录接口和注册接口
                .excludePathPatterns("/user/login","/user/register");
        */
    }


}
