package com.bjfu.li.odour.config;

import com.bjfu.li.odour.common.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/compound/news")
                .excludePathPatterns("/compound/all")
                .excludePathPatterns("/compound/search")
                .excludePathPatterns("/compound/advanced")
                .excludePathPatterns("/city/citySN");
    }
}
