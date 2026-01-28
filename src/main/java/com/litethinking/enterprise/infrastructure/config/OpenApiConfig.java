package com.litethinking.enterprise.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")
                        )
                )
                .info(new Info()
                        .title("Enterprise Management API")
                        .version("1.0.0")
                        .description("Production-grade REST API with Hexagonal Architecture for enterprise, product, and order management")
                        .contact(new Contact()
                                .name("Lite Thinking")
                                .email("soporte@litethinking.com")
                                .url("https://litethinking.com")
                        )
                        .license(new License()
                                .name("Proprietary")
                                .url("https://litethinking.com/license")
                        )
                );
    }
}
