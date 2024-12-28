package com.api.course.photoapp.api.api_gateway;

import java.util.Set;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Pre-Filter Component for API Gateway
 * ----------------------------------------
 * Purpose: Intercepts and processes all incoming requests before they reach the microservices
 * 
 * Features:
 * - Logs incoming request details
 * - Inspects request headers
 * - Provides request path information
 * - Maintains filter chain execution order
 * 
 * Notes:
 * - Implements GlobalFilter for gateway-wide request processing
 * - Uses Ordered interface to specify filter execution priority
 * - Utilizes Lombok's @Slf4j for automated logger creation
 */
@Component  // Marks this class as a Spring-managed component
@Slf4j      // Automatically creates a SLF4J logger instance
public class MyPreFilter implements GlobalFilter, Ordered {

    /**
     * Main Filter Method
     * ----------------------------------------
     * Processes each incoming HTTP request before it reaches its destination service.
     * 
     * Processing Steps:
     * 1. Logs filter execution start
     * 2. Extracts and logs request path
     * 3. Retrieves all request headers
     * 4. Logs each header name and value
     * 5. Forwards request to next filter in chain
     * 
     * @param exchange - Contains the HTTP request/response information
     *                  Provides access to request path, headers, and other HTTP elements
     * @param chain - The filter chain that allows the request to proceed to the next filter
     * 
     * @return Mono<Void> - Reactive response indicating filter completion
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Log the start of filter execution for request tracking
        log.info("My first pre-filter is executed...");

        // Extract and log the request path
        // This helps in debugging and monitoring request flow
        String requestPath = exchange.getRequest().getPath().toString();
        log.info("Request path = " + requestPath);

        // Get request headers for inspection
        // Headers contain important metadata about the request
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headerNames = headers.keySet();

        // Iterate through and log all headers and their values
        // This provides complete visibility of incoming request metadata
        headerNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            log.info(headerName + ":" + headerValue);
        });

        // Forward the request to the next filter in the chain
        // This ensures the request continues its journey through the gateway
        return chain.filter(exchange);
    }

    /**
     * Filter Order Definition
     * ----------------------------------------
     * Determines the execution order of this filter in the filter chain
     * 
     * Return Value:
     * - Returns 0 to indicate highest priority
     * - Lower numbers have higher priority
     * - Ensures this filter runs early in the chain
     * 
     * Note: The order is particularly important when multiple filters
     * need to execute in a specific sequence
     */
    @Override
    public int getOrder() {
        return 0;  // Highest priority - will execute before filters with higher numbers
    }

}
