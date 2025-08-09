package com.ftp.gateway.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * SUMMARY
 * -------
 * This is a Global Spring Cloud Gateway filter that:
 * - Bypasses CORS preflight and whitelisted paths.
 * - Validates JWT (signature + issuer + audience + clock skew).
 * - Extracts user roles from "roles" (array or CSV) or "role".
 * - On success: forwards the Bearer token and propagates claims (X-User-Id, X-Username, X-Roles) to downstream services.
 * - On failure: returns a JSON error with 401 (unauthorized) or 403 (forbidden).
 *
 * STEPS
 * -----
 * 1) Short-circuit CORS preflight (OPTIONS) and allow whitelisted paths without auth.
 * 2) Read and validate the "Authorization: Bearer <token>" header.
 * 3) Parse and validate the JWT using the configured secret, issuer, audience, and allowed clock skew.
 * 4) Extract roles; if none are present, deny access with 403.
 * 5) Mutate the request to forward Authorization and add helpful claim headers, then continue the filter chain.
 * 6) On any validation error, return a compact JSON response with the appropriate HTTP status code.
 */

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    // Secret used to verify HS256/HS512 tokens (must be >= 32 chars for HS256).
    @Value("${jwt.secret}")
    private String secretKey;

    // Optional issuer to enforce (empty means "do not check").
    @Value("${jwt.issuer:}")
    private String expectedIssuer;

    // Optional audience to enforce (empty means "do not check").
    @Value("${jwt.audience:}")
    private String expectedAudience;

    // Ant-style whitelist patterns (comma-separated in application.yml).
    @Value("#{'${gateway.whitelist:/auth/**,/swagger-ui/**,/v3/api-docs/**,/actuator/health}'.split(',')}")
    private List<String> whitelistPatterns;

    // Allowed clock skew (seconds) to tolerate small time drifts.
    @Value("${gateway.allowedClockSkewSeconds:60}")
    private long allowedClockSkewSeconds;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        final String path = exchange.getRequest().getURI().getPath();
        final String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "";


        // Step 1a) Allow all CORS preflight requests.
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        // Step 1b) Allow whitelisted paths (docs, health, login, etc.).
        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        // Step 2) Require "Authorization: Bearer <token>".
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return unauthorized(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7).trim();
        Claims claims;
        try {
            // Step 3a) Build a JWT parser with the HMAC key and clock skew.
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            var parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
                    .build();

            // Step 3b) Parse and validate the token (signature + exp/nbf).
            claims = parser.parseClaimsJws(token).getBody();

            // Step 3c) Optionally enforce issuer and audience if configured.
            if (!expectedIssuer.isBlank() && !expectedIssuer.equals(claims.getIssuer())) {
                return unauthorized(exchange, "Invalid token issuer");
            }
            if (!expectedAudience.isBlank()) {
                var aud = claims.getAudience();
                if (aud == null || !aud.equals(expectedAudience)) {
                    return unauthorized(exchange, "Invalid token audience");
                }
            }

            // Step 4) Extract roles; support "roles" (array/CSV) or single "role".
            List<String> roles = extractRoles(claims);
            if (roles.isEmpty()) {
                return forbidden(exchange, "No roles assigned");
            }

            // Step 5) Propagate token + helpful claims to downstream services.
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .header("X-User-Id", safeString(claims.get("userId", String.class), claims.getSubject()))
                    .header("X-Username", safeString(claims.get("username", String.class), claims.getSubject()))
                    .header("X-Roles", String.join(",", roles))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception ex) {
            // Step 6) Any parsing/validation error -> 401 with a compact JSON body.
            log.debug("Token validation failed: {}", ex.getMessage());
            return unauthorized(exchange, "Token validation failed");
        }
    }

    // Utility: check if the current path matches any whitelist pattern.
    private boolean isWhitelisted(String path) {
        for (String pattern : whitelistPatterns) {
            if (PATH_MATCHER.match(pattern.trim(), path)) {
                return true;
            }
        }
        return false;
    }

    // Utility: extract roles from "roles" (array or CSV) or fallback to "role" (single).
    private static List<String> extractRoles(Claims claims) {
        Object roleClaim = claims.get("roles");
        if (roleClaim instanceof List<?>) {
            return ((List<?>) roleClaim).stream().map(String::valueOf).collect(Collectors.toList());
        }
        if (roleClaim instanceof String s && !s.isBlank()) {
            return Arrays.stream(s.split(",")).map(String::trim).filter(v -> !v.isBlank()).collect(Collectors.toList());
        }
        String singleRole = claims.get("role", String.class);
        return (singleRole != null && !singleRole.isBlank()) ? List.of(singleRole) : List.of();
    }

    // Utility: choose a non-blank string (prefer explicit value, fallback to subject).
    private static String safeString(String preferred, String fallback) {
        return (preferred != null && !preferred.isBlank()) ? preferred : (fallback != null ? fallback : "");
    }

    // 401 helper.
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        return writeError(exchange, HttpStatus.UNAUTHORIZED, message);
    }

    // 403 helper.
    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        return writeError(exchange, HttpStatus.FORBIDDEN, message);
    }

    // Write a small JSON error body and status.
    private Mono<Void> writeError(ServerWebExchange exchange, HttpStatus status, String message) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = ("{\"error\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)))
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(e -> Mono.empty());
    }

    // Run this filter as early as possible.
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
