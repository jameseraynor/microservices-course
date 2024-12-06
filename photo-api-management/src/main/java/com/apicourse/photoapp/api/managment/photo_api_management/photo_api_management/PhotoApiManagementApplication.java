package com.apicourse.photoapp.api.managment.photo_api_management.photo_api_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PhotoApiManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoApiManagementApplication.class, args);
	}

}
