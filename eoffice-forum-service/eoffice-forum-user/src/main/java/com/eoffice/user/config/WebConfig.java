package com.eoffice.user.config;


import com.eoffice.user.interceptors.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final TokenInterceptor tokenInterceptor;

    public WebConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                //设置拦截的路径
                .addPathPatterns("/**")
                //设置排除的路径登录接口和注册接口，获取验证码接口，重置密码接口
                .excludePathPatterns("/user/login", "/user/register","/user/nickName","/user/getResetCode","/user/resetPassword");


    }
}
