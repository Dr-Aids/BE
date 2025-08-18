package com.example.dr_aids.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // allowedOrigins → http/https 모두 정확히 지정
                        .allowedOriginPatterns(
                                "http://localhost:3000",
                                "https://localhost:3000",
                                "http://localhost:8080",
                                "https://localhost:8080",
                                "https://draids.site",
                                "https://*.draids.site"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization", "Location")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}