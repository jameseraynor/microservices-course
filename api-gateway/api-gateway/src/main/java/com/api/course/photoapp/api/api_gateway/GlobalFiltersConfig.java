package com.api.course.photoapp.api.api_gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GlobalFiltersConfig  {

    @Bean
    @Order(1)
    GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            log.info("My second global post-filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My third global post-filter is executed...");
            }));
        };
    }

    @Bean
    @Order(2)
    GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            log.info("My third global post-filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My second global post-filter is executed...");
            }));
        };
    }

    @Bean
    @Order(3)
    GlobalFilter fourthPreFilter() {
        return (exchange, chain) -> {
            log.info("My fourth global post-filter is executed...");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("My first global post-filter is executed...");
            }));
        };
    }



}
