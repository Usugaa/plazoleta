package com.microservicio.restaurant.infraestructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .info(new Info()
                                .title("API de Restaurantes")
                                .description("API para la gestión de restaurantes")
                                .version("1.0")
                                .contact(new Contact()
                                        .name("Pragma")
                                        .email("carlos@pragma.com")
                                        .url("www.pragma.com")))
                        .addSecurityItem(new SecurityRequirement().addList("JWT"))
                        .components(new Components()
                                .addSecuritySchemes("JWT", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")));
        }
}
