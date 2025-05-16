package com.gram.eureka.eureka_gram_master.config;


import com.gram.eureka.eureka_gram_master.filter.JwtAuthenticationFilter;
import com.gram.eureka.eureka_gram_master.service.impl.CustomUserDetailsServiceImpl;
import com.gram.eureka.eureka_gram_master.util.AuthExceptionHandlerUtil;
import com.gram.eureka.eureka_gram_master.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private final AuthExceptionHandlerUtil authExceptionHandlerUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 화이트리스트 (인증 없이 접근 허용할 경로)
        String[] whiteList = {
                "/",
                "/login",
                "/users/**",
                "/page/**",

                "/css/**",
                "/html/**",
                "/images/**",
                "/js/**",
                "/logo/**",

                "/index.html",
                "/favicon.ico"
        };

        // 블랙리스트 (인증 필요, 추가적인 제한 걸 경로 – 예: 관리자 등급)
        String[] blackList = {
                "/**"
        };

        http
                // CSRF 보호 비활성화 (JWT 기반이므로 필요 없음)
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 기본 설정 적용 (필요 시 커스터마이징 가능)
                .cors(Customizer.withDefaults())

                // 폼 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable)

                // HTTP Basic 인증도 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)

                // 세션을 생성하지 않고, 완전한 무상태(stateless) 방식 사용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청별 인증/인가 정책 정의
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteList).permitAll()               // 화이트리스트 경로는 모두 허용
                        .requestMatchers(blackList).authenticated()           // 블랙리스트 경로는 인증 필요
                        .anyRequest().permitAll()                             // 나머지는 모두 허용 (필요 시 변경)
                )

                // 인증 실패시에 로그인 페이지로 이동 설정
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authExceptionHandlerUtil) // 등록
                )

                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 등록
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                )

                // 사용자 인증 시 사용할 CustomUserDetailsService 등록
                .userDetailsService(customUserDetailsServiceImpl);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
