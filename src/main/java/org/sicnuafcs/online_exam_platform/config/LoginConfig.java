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
                .excludePathPatterns("/login/id")
                .excludePathPatterns("/login/phone")
                .excludePathPatterns("/login/get/userInfo")
                .excludePathPatterns("/login/logout")
                .excludePathPatterns("/register/student")
                .excludePathPatterns("/register/teacher")
                .excludePathPatterns("/register/email/student")
                .excludePathPatterns("/register/email/teacher")
                .excludePathPatterns("/exam/writeToDocker")
                .excludePathPatterns("/exam/judgeProgram");
    }
}
