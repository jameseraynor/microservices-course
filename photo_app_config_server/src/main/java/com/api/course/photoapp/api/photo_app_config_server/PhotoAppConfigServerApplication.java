package com.api.course.photoapp.api.photo_app_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableConfigServer
@RefreshScope
public class PhotoAppConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppConfigServerApplication.class, args);
	}

}
