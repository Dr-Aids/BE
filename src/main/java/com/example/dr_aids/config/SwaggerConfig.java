package com.example.dr_aids.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0") //버전
                .title("Dr-Aids API") //이름
                .description("AI 전자 차트 요약 및 낭독 API"); //설명
        return new OpenAPI()
                .info(info);
    }

}