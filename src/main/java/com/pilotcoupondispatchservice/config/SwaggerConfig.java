package com.pilotcoupondispatchservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 3.0 Configuration
 * Provides API documentation available at: http://localhost:8080/swagger-ui.html
 * JSON spec available at: http://localhost:8080/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token from login endpoint")
                        )
                )
                .info(new Info()
                        .title("Pilot Coupon Dispatch Service API")
                        .version("1.0.0")
                        .description("Pilot Coupon Dispatch Service API - Complete system for managing pilot coupon dispatching")
                        .contact(new Contact()
                                .name("Pilot Coupon Dispatch Service Team")
                                .email("support@pilotcoupondispatchservice.com")
                                .url("https://pilotcoupondispatchservice.com")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}
