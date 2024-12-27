package com.apicourse.photapp.api.users.photo_api_users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.apicourse.photapp.api.users.photo_api_users.shared.FeignErrorDecoder;

import feign.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
@EnableFeignClients
public class PhotoApiUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoApiUsersApplication.class, args);
	}

	@Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        // Creates a bean for password encryption using BCrypt hashing algorithm
        // This encoder will be used for securely hashing user passwords before storage
        return new BCryptPasswordEncoder();
    }

    @Bean
    HttpExchangeRepository httpExchangeRepository() {
        // Creates a bean for storing HttpExchange objects
        // This repository will be used to store HttpExchange objects for later retrieval
        return new InMemoryHttpExchangeRepository();
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        // Creates a bean for making RESTful API calls
        // This bean will be used to make RESTful API calls to other services
        return new RestTemplate();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        // Creates a bean for setting the log level of Feign client
        // This bean will be used to set the log level of Feign client to FULL
        return Logger.Level.FULL;
    }

    @Bean
    public FeignErrorDecoder errorDecoder() {
        // Creates a bean for decoding Feign client errors
        // This bean will be used to decode Feign client errors
        return new FeignErrorDecoder();
    }
}
