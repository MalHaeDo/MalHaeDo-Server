package com.backend.malhaedo.global.config;

import com.backend.malhaedo.global.jwt.filter.JwtFilter;
import com.backend.malhaedo.global.jwt.handler.JwtAccessDeniedHandler;
import com.backend.malhaedo.global.jwt.handler.JwtAuthenticationEntryPoint;
import com.backend.malhaedo.global.oauth.handler.OAuthLoginFailureHandler;
import com.backend.malhaedo.global.oauth.handler.OAuthLoginSuccessHandler;
import com.backend.malhaedo.global.oauth.service.PrincipalOauth2UserService;
import com.backend.malhaedo.global.util.JwtUtil;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // OAuth2 로그인 성공 시 handler
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    // OAuth2 로그인 실패 시 handler
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;
    // OAuth2 유저 처리 handler
    private final PrincipalOauth2UserService principalOauth2UserService;
    // 인가에 실패한 경우 실행할 예외 처리 handler
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    // 인증에 실패한 경우 실행할 예외 처리 Handler
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtUtil jwtProvider;
    private final UserDetailsService userDetailsService;

    // 허용할 URL을 배열의 형태로 관리
    public static final String[] allowUrl = {
            "/",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v0/member/signup",
            "/api/v0/member/signup/guest",
            "/api/v0/auth/kakao",
            "/api/v0/auth/google",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .authorizeHttpRequests(request -> request
                        // 인증 없이 허용할 경로 추가
                        .requestMatchers("/.well-known/acme-challenge/**").permitAll()
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .oauth2Login(oauth ->
                        oauth
                                .successHandler(oAuthLoginSuccessHandler)
                                .failureHandler(oAuthLoginFailureHandler)
                                .userInfoEndpoint(userInfo ->
                                        userInfo.userService(principalOauth2UserService)
                                )
                );

        return http.build();
    }

    @Bean
    public Filter jwtFilter() {
        return new JwtFilter(jwtProvider, userDetailsService);
    }
}
