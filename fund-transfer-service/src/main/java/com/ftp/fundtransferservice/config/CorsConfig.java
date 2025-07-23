package com.ftp.fundtransferservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration class.
 * This class configures Cross-Origin Resource Sharing (CORS) for the application.
 * CORS allows resources to be shared between different origins, useful when the front-end
 * and back-end applications are running on different servers or ports.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for the application.
     * It allows cross-origin requests to all paths in the application (defined by "/**").
     *
     * @param registry the CorsRegistry used to configure CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all paths
                .allowedOrigins("http://localhost:9090") // Allow requests from this origin (front-end server)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow sending credentials (e.g., cookies, authorization headers)
    }
}
