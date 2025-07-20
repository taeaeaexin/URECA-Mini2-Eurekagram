package com.gram.eureka.eureka_gram_user.config;

import com.gram.eureka.eureka_gram_user.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/css/**",
                "/html/**",
                "/images/**",
                "/js/**",
                "/index.html",
                "/favicon.ico"
        };
        String[] blackList = {
                "/**"
        };

        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns(blackList) // 모든 경로에 대해 Interceptor 적용
                .excludePathPatterns(whiteList); // 특정 경로에 대해 Interceptor 미 적용
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스 핸들러 추가
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/static/images/");
    }
}
