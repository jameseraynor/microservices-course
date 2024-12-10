package com.apicourse.photapp.api.users.photo_api_users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class PhotoApiUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoApiUsersApplication.class, args);
	}

	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        // Creates a bean for password encryption using BCrypt hashing algorithm
        // This encoder will be used for securely hashing user passwords before storage
        return new BCryptPasswordEncoder();
    }


}
