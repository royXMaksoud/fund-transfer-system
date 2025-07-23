package com.ftp.fundtransferservice.config;

import com.ftp.fundtransferservice.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig class is responsible for configuring security settings for the application.
 * It includes configuring HTTP security, JWT authentication, and access permissions for various endpoints.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor to inject the JwtAuthenticationFilter dependency.
     *
     * @param jwtAuthenticationFilter the JWT authentication filter to be used for request authentication
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the HTTP security for the application.
     * - Disables CSRF protection (typically not recommended for REST APIs, but can be used for non-browser clients).
     * - Allows unauthenticated access to Swagger UI, OpenAPI docs, and H2 Console.
     * - Disables frame options to allow H2 Console rendering.
     * - All other requests are authenticated.
     * - Adds the JwtAuthenticationFilter to the filter chain before UsernamePasswordAuthenticationFilter.
     *
     * @param http HttpSecurity object used to configure web-based security.
     * @return the configured SecurityFilterChain
     * @throws Exception if the security configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF and exclude H2 console from CSRF protection
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable()
                )
                // Disable default headers protection to allow H2 console in iframe (replaces deprecated frameOptions)
                .headers(headers -> headers.disable())
                // Configure access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // Add JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
