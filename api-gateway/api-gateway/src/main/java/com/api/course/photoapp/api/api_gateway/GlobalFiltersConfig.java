package com.api.course.photoapp.api.api_gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Global Filters Configuration for API Gateway
 * This class configures global filters that are applied to all routes in the gateway.
 * Each filter is executed in order based on its @Order annotation (lower numbers execute first).
 *
 * @Configuration marks this class as a source of bean definitions
 * @Slf4j enables automatic creation of SLF4J Logger field
 */
@Configuration
@Slf4j
public class GlobalFiltersConfig {

    /**
     * Second Pre-Filter in the chain
     * This filter executes both pre and post-filtering operations
     * Pre-filter order: 1 (executes second in pre-filter chain)
     * Post-filter execution is handled in the then() block
     *
     * @return GlobalFilter that logs pre and post filter execution
     */
    @Bean
    @Order(1)
    GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            // Pre-filter execution
            log.info("My second global post-filter is executed...");

            // Continue the filter chain and add post-filter operation
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        // Post-filter execution
                        log.info("My third global post-filter is executed...");
                    }));
        };
    }

    /**
     * Third Pre-Filter in the chain
     * This filter executes both pre and post-filtering operations
     * Pre-filter order: 2 (executes third in pre-filter chain)
     * Post-filter execution is handled in the then() block
     *
     * @return GlobalFilter that logs pre and post filter execution
     */
    @Bean
    @Order(2)
    GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            // Pre-filter execution
            log.info("My third global post-filter is executed...");

            // Continue the filter chain and add post-filter operation
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        // Post-filter execution
                        log.info("My second global post-filter is executed...");
                    }));
        };
    }

    /**
     * Fourth Pre-Filter in the chain
     * This filter executes both pre and post-filtering operations
     * Pre-filter order: 3 (executes fourth in pre-filter chain)
     * Post-filter execution is handled in the then() block
     *
     * @return GlobalFilter that logs pre and post filter execution
     */
    @Bean
    @Order(3)
    GlobalFilter fourthPreFilter() {
        return (exchange, chain) -> {
            // Pre-filter execution
            log.info("My fourth global post-filter is executed...");

            // Continue the filter chain and add post-filter operation
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        // Post-filter execution
                        log.info("My first global post-filter is executed...");
                    }));
        };
    }
}

/**
 * Filter Execution Order Explanation:
 * 
 * Pre-filter execution order (based on @Order annotation):
 * 1. @Order(1) - secondPreFilter
 * 2. @Order(2) - thirdPreFilter
 * 3. @Order(3) - fourthPreFilter
 *
 * Post-filter execution order (reverse of pre-filter):
 * 1. fourthPreFilter post execution
 * 2. thirdPreFilter post execution
 * 3. secondPreFilter post execution
 *
 * Request Flow:
 * 1. Request comes in
 * 2. Pre-filters execute in @Order (ascending)
 * 3. Request is processed
 * 4. Post-filters execute in reverse @Order (descending)
 * 5. Response is sent
 *
 * Note: The naming of the log messages might be confusing as they don't match
 * the actual execution order. Consider renaming the log messages to match
 * the actual execution sequence for better clarity.
 */
