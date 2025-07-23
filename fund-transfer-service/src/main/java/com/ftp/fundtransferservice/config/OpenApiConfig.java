package com.ftp.fundtransferservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfig is the configuration class for setting up Swagger OpenAPI documentation.
 * It configures how the API documentation is generated and customized for the "Auth Service API".
 */
@Configuration
public class OpenApiConfig {

    /**
     * Bean to configure custom OpenAPI settings.
     * This method sets the API documentation's title, version, description, and security scheme.
     *
     * @return OpenAPI custom configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Set the API metadata (title, version, description)
                .info(new Info()
                        .title("Auth Service API") // The title of the API
                        .version("1.0.0") // The version of the API
                        .description("Documentation for Auth Service")) // A short description of the API
                // Add the security requirement to the API
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // Add security schemes (Bearer Authentication for JWT)
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP) // The security type is HTTP
                                .scheme("bearer") // The authentication scheme is Bearer
                                .bearerFormat("JWT"))); // Specify the format of the bearer token (JWT)
    }
}
