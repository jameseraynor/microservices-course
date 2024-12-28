package com.api.course.photoapp.api.photo_app_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Configuration Server Application for Microservices Architecture
 * =================================================================
 * This class serves as the entry point for the Spring Cloud Config Server,
 * which provides centralized configuration management for all microservices
 * in the photo application ecosystem.
 * 
 * Key Features:
 * ------------------
 * 1. Configuration Management:
 *    - Centralizes configuration for all microservices
 *    - Supports dynamic configuration updates
 *    - Manages environment-specific properties
 * 
 * 2. Server Capabilities:
 *    - Acts as a central configuration repository
 *    - Serves configuration to client applications
 *    - Supports version control of configurations
 * 
 * Annotations:
 * ------------------
 * @SpringBootApplication
 *    - Enables Spring Boot auto-configuration
 *    - Activates component scanning
 *    - Configures the application context
 * 
 * @EnableConfigServer
 *    - Activates Spring Cloud Config Server
 *    - Enables configuration serving capabilities
 *    - Sets up necessary endpoints
 * 
 * Technical Details:
 * ------------------
 * - Runs as a standalone configuration server
 * - Supports multiple backend sources (Git, File System, etc.)
 * - Provides RESTful access to configurations
 * - Enables encryption/decryption of properties
 */
@SpringBootApplication
@EnableConfigServer
public class PhotoAppConfigServerApplication {

	/**
     * Application Entry Point
     * =================================================================
     * Main method that bootstraps the Spring Cloud Config Server.
     * 
     * Functionality:
     * ------------------
     * 1. Initializes the Spring application context
     * 2. Starts the embedded web server
     * 3. Sets up the configuration server endpoints
     * 4. Prepares for serving configurations
     * 
     * Server Startup Process:
     * ------------------
     * 1. Spring Boot initialization
     * 2. Configuration server setup
     * 3. Endpoint exposure
     * 4. Service registry integration (if configured)
     * 
     * @param args Command line arguments passed to the application
     *            - Can include custom spring properties
     *            - Supports profile activation
     *            - Enables runtime configuration
     */
	public static void main(String[] args) {
		// Initialize and start the Spring Boot application
        // This single line handles:
        // - Creation of application context
        // - Component scanning
        // - Auto-configuration
        // - Starting embedded server
		SpringApplication.run(PhotoAppConfigServerApplication.class, args);
	}

}
