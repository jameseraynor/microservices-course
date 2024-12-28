package com.api.course.photoapp.api.api_gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Post-Filter Implementation for API Gateway
 * This filter executes after the request has been processed and before the response is sent back to the client.
 * 
 * @Component marks this class as a Spring-managed component for automatic detection and registration
 * @Slf4j provides automatic logging capability through Lombok
 */
@Component
@Slf4j
public class MyPostFilter implements GlobalFilter, Ordered {

    /**
     * Filter implementation that executes after request processing
     * This method is called for every request passing through the gateway
     *
     * @param exchange ServerWebExchange object containing HTTP request and response information
     * @param chain GatewayFilterChain to pass the exchange to the next filter in the chain
     * @return Mono<Void> representing the completion of filter processing
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Execute the filter chain first
        return chain.filter(exchange)
                   // After the chain completes, execute our post-filter logic
                   .then(Mono.fromRunnable(() -> {
                       // Log post-filter execution
                       // Consider adding more context information like request path, status code, etc.
                       log.info("My last post-filter is executed...");
                       
                       // You could add additional post-processing here, such as:
                       // - Response header modification
                       // - Metrics collection
                       // - Response logging
                       // - Error handling
                   }));
    }

    /**
     * Defines the order of filter execution
     * Lower values have higher priority
     * 
     * @return int representing the filter's order in the chain
     */
    @Override
    public int getOrder() {
       return 0;  // Highest priority
    }
}
