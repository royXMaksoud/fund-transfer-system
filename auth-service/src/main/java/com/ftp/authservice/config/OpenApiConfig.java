package com.ftp.authservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfig class configures the OpenAPI (Swagger) documentation for the Auth Service API.
 * It defines the general information about the API, including title, version, and description.
 * Additionally, it specifies security requirements for endpoints (JWT Bearer authentication).
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation for the Auth Service.
     * This method provides metadata like the API title, version, description, and security scheme.
     *
     * @return an OpenAPI object containing the configuration for Swagger UI.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth Service API")  // Sets the title of the API documentation.
                        .version("1.0.0")  // Sets the version of the API.
                        .description("Documentation for Auth Service"))  // Description of the API.
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))  // Defines the security requirement (JWT Bearer token).
                .components(new Components().addSecuritySchemes("bearerAuth",  // Adds the security scheme for JWT.
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)  // The scheme type is HTTP.
                                .scheme("bearer")  // Specifies the "bearer" authentication scheme.
                                .bearerFormat("JWT")));  // Specifies that the bearer token is in JWT format.
    }
}
