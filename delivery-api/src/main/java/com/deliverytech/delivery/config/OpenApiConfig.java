package com.deliverytech.delivery.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("DeliveryTech API")
            .version("1.0.0")
            .description("API for managing delivery operations")
            .contact(new Contact()
                .name("DeliveryTech Support")
                .email("dev@deliverytech.com")
                .url("https://www.deliverytech.com/support"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")))
        .servers(List.of(
            new Server()
                .url("http://localhost:8080")
                .description("Local Development Server"),
            new Server()
                .url("https://api.deliverytech.com/v1")
                .description("Production Server")))
        .components(
            new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
        )
        .addSecurityItem(
            new SecurityRequirement().addList("bearerAuth")
        );
  }
}
