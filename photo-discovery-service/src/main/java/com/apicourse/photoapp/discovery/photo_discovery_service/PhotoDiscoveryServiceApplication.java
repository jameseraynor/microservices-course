package com.apicourse.photoapp.discovery.photo_discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PhotoDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoDiscoveryServiceApplication.class, args);
	}

}
