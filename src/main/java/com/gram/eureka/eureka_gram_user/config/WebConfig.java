package com.gram.eureka.eureka_gram_user.config;

import com.gram.eureka.eureka_gram_user.interceptor.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {"/**"};
        String[] blackList = {
                "/css/**",
                "/html/**",
                "/image/**",
                "/js/**",
                "index.html",
                "favicon.ico"
        };

        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns(whiteList) // 모든 경로에 대해 Interceptor 적용
                .excludePathPatterns(blackList); // 특정 경로에 대해 Interceptor 미 적용
    }
}
