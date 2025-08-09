package com.ftp.gateway.gatewayservice.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration for the API Gateway.
 *
 * Purpose:
 * - Defines the main API documentation for the Gateway.
 * - Groups APIs from multiple microservices so they can be accessed from a single Swagger UI page.
 * - Helps aggregate and organize routes for different services behind the Gateway.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates the main OpenAPI configuration for the Gateway itself.
     * This sets the global title, description, and version that will appear in Swagger UI.
     */
    @Bean
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway Documentation")
                        .description("Swagger UI for API Gateway - Aggregating microservices APIs")
                        .version("1.0.0"));
    }

    /**
     * Defines a Swagger group for the Gateway's own endpoints.
     * This will match and include all routes (/**) that belong to the Gateway itself.
     */
    @Bean
    public GroupedOpenApi gatewayGroup() {
        return GroupedOpenApi.builder()
                .group("gateway") // Group name shown in Swagger UI dropdown
                .pathsToMatch("/**") // Matches all paths in the Gateway
                .build();
    }

    /**
     * Defines a Swagger group for the Auth Service endpoints.
     * All routes starting with /auth/** will appear in this group in Swagger UI.
     * The Gateway will proxy these requests to the Auth Service.
     */
    @Bean
    public GroupedOpenApi authServiceGroup() {
        return GroupedOpenApi.builder()
                .group("auth-service")
                .pathsToMatch("/auth/**")
                .build();
    }

    /**
     * Defines a Swagger group for the Fund Transfer Service endpoints.
     * All routes starting with /transfers/** will appear in this group in Swagger UI.
     * The Gateway will proxy these requests to the Fund Transfer Service.
     */
    @Bean
    public GroupedOpenApi transferServiceGroup() {
        return GroupedOpenApi.builder()
                .group("fund-transfer-service")
                .pathsToMatch("/transfers/**")
                .build();
    }

}
