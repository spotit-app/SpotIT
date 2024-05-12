package com.spotit.backend.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserAuth0IdInterceptor userAuth0IdInterceptor;

    public WebMvcConfig(UserAuth0IdInterceptor userAuth0IdInterceptor) {
        this.userAuth0IdInterceptor = userAuth0IdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuth0IdInterceptor)
                .addPathPatterns("/api/userAccount/**");
    }
}
