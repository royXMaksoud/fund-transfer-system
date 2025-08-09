package com.ftp.gateway.gatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * SUMMARY
 * -------
 * A custom Spring Cloud Gateway filter factory that logs requests and responses
 * for any route that includes `- name: Logging` in application.yml.
 *
 * PURPOSE
 * -------
 * - Records the HTTP method, path, route ID, and response status.
 * - Measures and logs the execution time for the request through the gateway.
 * - Helps debug traffic and performance at the gateway layer.
 *
 * HOW IT WORKS
 * ------------
 * 1. Before forwarding: Logs the request method, path, and routeId.
 * 2. After response returns: Logs the status code, and time taken.
 * 3. Uses SLF4J logging instead of System.out for production readiness.
 */

@Component
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {
    // Logger for debug/info output
    private static final Logger log = LoggerFactory.getLogger(LoggingGatewayFilterFactory.class);

    // Constructor - tell the base factory this filter has a Config class
    public LoggingGatewayFilterFactory() { super(Config.class); }

    // Empty config class now (placeholder for future options)
    public static class Config {
        // Example: boolean logHeaders; String logLevel;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // STEP 1: Capture start time for performance measurement
            long start = System.currentTimeMillis();

            // STEP 2: Extract request info (method, path)
            var req = exchange.getRequest();
            String method = req.getMethod().name();
            String path = req.getURI().getPath();

            // STEP 3: Get route ID from Gateway context (default to "unknown")
            String routeId = exchange.getAttributeOrDefault(
                    "org.springframework.cloud.gateway.support.ServerWebExchangeUtils.gatewayRouteId",
                    "unknown"
            );

            // STEP 4: Log incoming request details
            log.debug(" [{}] {} (routeId={})", method, path, routeId);

            // STEP 5: Continue filter chain, then log response details after completion
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                var res = exchange.getResponse();
                int status = res.getStatusCode() != null ? res.getStatusCode().value() : 200;

                // Calculate time taken
                long took = System.currentTimeMillis() - start;

                // Log outgoing response details
                log.debug("[{}] {} -> {} ({} ms, routeId={})", method, path, status, took, routeId);
            }));
        };
    }
}
