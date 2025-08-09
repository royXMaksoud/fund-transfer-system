package com.ftp.gateway.gatewayservice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class GatewaySecurityConfig {
    /**
     * GatewaySecurityConfig
     * ---------------------
     * Spring Security configuration for the API Gateway.
     * <p>
     * PURPOSE:
     * - Disable default login forms and HTTP Basic authentication.
     * - Disable CSRF protection (not needed for stateless APIs).
     * - Allow all requests to pass through Gateway without blocking.
     * - This ensures no browser pop-up login or default Spring Security login page appears.
     * <p>
     * NOTE:
     * - Real authentication/authorization should be handled via a custom AuthenticationFilter or JWT verification.
     */

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(
                                // auth endpoints
                                "/auth/**",
                                // gateway swagger ui & static assets
                                "/swagger-ui.html", "/swagger-ui/**", "/webjars/**",
                                // openapi docs (gateway + proxied services)
                                "/v3/api-docs", "/v3/api-docs/**",
                                "/auth/v3/api-docs", "/auth/v3/api-docs/**",
                                "/transfers/v3/api-docs", "/transfers/v3/api-docs/**",
                                // health
                                "/actuator/health"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }

}
