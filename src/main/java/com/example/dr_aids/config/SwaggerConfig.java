package com.example.dr_aids.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;   // ★추가
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List; // ★추가

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Security Requirement
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        // ★ 서버 목록(드롭다운에 표시됨)
        Server dev = new Server().url("https://draids.site").description("Production (HTTPS)");
        Server local = new Server().url("http://localhost:8080").description("Local Dev (HTTP)");

        return new OpenAPI()
                .info(new Info()
                        .title("Dr. Aids API")
                        .version("v1.0")
                        .description("Dr. Aids API 문서입니다."))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("BearerAuth", securityScheme)
                .servers(List.of(dev, local));  // ★여기!
    }
}
