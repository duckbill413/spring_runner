package com.example.runner.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    String[] WHITE_LIST_URL = {
            "/webjars/**", "/images/**", "/css/**", "/h2-console/**",
            "/login"
    };
    private final AccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST_URL).permitAll() // WHITE_LIST_URL 의 모든 경로에 대하여 접근 허용
                        .requestMatchers("/delete/**").hasRole("ADMIN") // ADMIN 권한을 가져야 /delete/** 경로 접근 가능
                        .anyRequest().authenticated()
                ).formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer.loginPage("/login")
                ).exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(customAccessDeniedHandler)
                )
        ;

        return http.build();
    }

    /**
     * UserDetails 를 통해 user, admin 유저를 생성
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withUsername("user")
                .passwordEncoder(passwordEncoder()::encode)
                .password("duckbill")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .passwordEncoder(passwordEncoder()::encode)
                .username("admin")
                .password("duckbill")
                .roles("USER", "ADMIN")
                .build();

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);
        return userDetailsManager;
    }

    /**
     * 비밀번호 암호화 알고리즘
     * 스프링에서는 다양한 비밀번호 암호화 알고리즘을 지원
     * 1. NoOpPasswordEncoder
     * 2. BCryptPasswordEncoder
     * 3. Pbkdf2PasswordEncoder
     * 4. ScryptPasswordEncoder
     * 스프링 시큐리티는 PasswordEncoderFactories 클래스를 제공해주므로 개발자는 이를 이용해 DelegatingPasswordEncoder 인스턴스를 생성
     * DelegatingPasswordEncoder는 PasswordEncoder 구현체에게 암호화 처리를 위임
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 내부적으로 안정성이 높다고 알려진 BCryptPasswordEncoder를 사용
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 spring security 대상에서 제외
        return (web) -> {
            web.ignoring()
                    .requestMatchers(
                            PathRequest.toStaticResources().atCommonLocations()
                    ).requestMatchers("/favicon.ico", "/resources/**", "/error");
        };
    }
}
