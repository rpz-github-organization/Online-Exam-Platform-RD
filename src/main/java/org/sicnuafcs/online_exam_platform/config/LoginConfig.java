package org.sicnuafcs.online_exam_platform.config;

import org.sicnuafcs.online_exam_platform.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/api/login/id")
                .excludePathPatterns("/api/login/phone")
                .excludePathPatterns("/api/login/get/session")
                .excludePathPatterns("/api/login/get/All/session")
                .excludePathPatterns("/api/login/logout")
                .excludePathPatterns("/api/register/student")
                .excludePathPatterns("/api/register/teacher")
                .excludePathPatterns("/api/register/email/student")
                .excludePathPatterns("/api/register/email/teacher");
    }
}
