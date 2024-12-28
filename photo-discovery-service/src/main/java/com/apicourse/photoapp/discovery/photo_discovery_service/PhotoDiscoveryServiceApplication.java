package com.apicourse.photoapp.discovery.photo_discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Photo Discovery Service Application
 * =================================================================
 * Main class for the Eureka Discovery Server application.
 * Provides service registration and discovery for the microservices
 * architecture.
 * 
 * Annotations:
 * ------------------
 * @SpringBootApplication - Enables Spring Boot auto-configuration
 * @EnableEurekaServer   - Configures this application as Eureka Server
 * @RefreshScope        - Enables runtime configuration refresh
 * 
 * Features:
 * ------------------
 * 1. Service Registry:
 *    - Maintains registry of microservices
 *    - Tracks service instances
 *    - Manages service metadata
 * 
 * 2. Service Discovery:
 *    - Enables service lookup
 *    - Supports load balancing
 *    - Facilitates service-to-service communication
 * 
 * 3. High Availability:
 *    - Supports peer awareness
 *    - Enables registry replication
 *    - Provides failover capability
 * 
 * Architecture Role:
 * ------------------
 * - Central service registry
 * - Discovery server
 * - Configuration management
 * - Service coordination
 */
@SpringBootApplication
@EnableEurekaServer
@RefreshScope
public class PhotoDiscoveryServiceApplication {

    /**
     * Application Entry Point
     * =================================================================
     * Bootstraps the Eureka Server application
     * 
     * Purpose:
     * ------------------
     * - Initializes Spring Boot application
     * - Starts Eureka Server
     * - Enables service registry
     * 
     * Configuration:
     * ------------------
     * - Loads application properties
     * - Configures server settings
     * - Initializes discovery service
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(PhotoDiscoveryServiceApplication.class, args);
    }
}
