package com.apicourse.photoapp.api.managment.photo_api_management.photo_api_management.ui.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class ManagementController {
	
	@GetMapping("/status/check")
	public String status() {
		return "Working";
	}
}
