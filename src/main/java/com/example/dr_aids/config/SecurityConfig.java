package com.example.dr_aids.config;

import com.example.dr_aids.security.common.AIProperties;
import com.example.dr_aids.security.common.JWTUtil;
import com.example.dr_aids.security.filter.AIRequestFilter;
import com.example.dr_aids.security.filter.CustomLogoutFilter;
import com.example.dr_aids.security.filter.JWTFilter;
import com.example.dr_aids.security.filter.LoginFilter;
import com.example.dr_aids.security.repository.RefreshTokenRepository;
import com.example.dr_aids.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshRepository;
    private final AIRequestFilter aiRequestFilter;

    private final AIProperties aiProperties;


    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil,
                          RefreshTokenRepository refreshTokenRepository, UserRepository userRepository,
                           AIRequestFilter aiRequestFilter, AIProperties aiProperties) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.aiRequestFilter = aiRequestFilter;
        this.aiProperties = aiProperties;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // 프론트/스웨거/배포 도메인 (http/https 모두)
                    config.setAllowedOriginPatterns(Arrays.asList(
                            "http://localhost:3000",
                            "https://localhost:3000",
                            "http://localhost:8080",
                            "https://localhost:8080",
                            "https://draids.site",
                            "https://*.draids.site"
                    ));
                    // ← OPTIONS 반드시 포함
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    config.setAllowCredentials(true);
                    // 프리플라이트 헤더 전부 허용
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(Arrays.asList("Authorization", "Location"));
                    config.setMaxAge(3600L); // 캐싱 시간
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 프리플라이트 전부 허용 (중요)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/login", "/join", "/reissue",
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**",

                                // AI 경로 화이트리스트
                                "/patient/list/ai",
                                "/prescriptions/ai",
                                "/blood-test/ai"
                        ).permitAll()
                        .anyRequest().authenticated() //개발 중
                );
        // JWT 필터 & 커스텀 필터 설정
        http.addFilterBefore(aiRequestFilter, UsernamePasswordAuthenticationFilter.class);// 먼저 AI 필터 실행
        http.addFilterBefore(new JWTFilter(jwtUtil, userRepository, aiProperties.getWhitelistPaths()), LoginFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);



        return http.build();
    }
}

