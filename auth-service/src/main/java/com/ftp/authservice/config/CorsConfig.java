package com.ftp.authservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig class is used to configure Cross-Origin Resource Sharing (CORS) settings for the application.
 * This configuration allows the server to specify which domains can access the resources.
 * It is necessary for enabling the front-end applications hosted on different origins to interact with the backend API.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * This method configures CORS mappings for the application.
     * It allows the specified origins to make requests to the backend and ensures that the right methods,
     * headers, and credentials are allowed for cross-origin requests.
     *
     * @param registry The CORS registry used to define the mappings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allows all paths (/**) to be accessible from the specified origin (http://localhost:9090).
        registry.addMapping("/**")
                // Allows requests from the specified origin (in this case, localhost at port 9090).
                .allowedOrigins("http://localhost:9090")
                // Specifies which HTTP methods are allowed (GET, POST, PUT, DELETE, OPTIONS).
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Allows all headers to be included in the requests.
                .allowedHeaders("*")
                // Allows credentials (cookies, authorization headers) to be included in the requests.
                .allowCredentials(true);
    }
}
