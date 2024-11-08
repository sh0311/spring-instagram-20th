package com.ceos20.instagram.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){ //Swagger 문서의 설정을 정의
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo()) //API 정보(제목, 설명, 버전, ..)을 설정
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    
    private Info apiInfo(){
        return new Info()
                .title("Springdoc 테스트")   // API의 제목
                .description("Springdoc을 사용한 Swagger UI 테스트")  // API에 대한 설명
                .version("1.0.0");
    }
}
