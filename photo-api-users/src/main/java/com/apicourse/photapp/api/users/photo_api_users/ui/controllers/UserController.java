package com.apicourse.photapp.api.users.photo_api_users.ui.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apicourse.photapp.api.users.photo_api_users.service.UserService;
import com.apicourse.photapp.api.users.photo_api_users.shared.UserDto;
import com.apicourse.photapp.api.users.photo_api_users.ui.model.CreateUserRequestModel;
import com.apicourse.photapp.api.users.photo_api_users.ui.model.CreateUserResponseModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    Environment environment;

    @Autowired
    UserService service;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetail) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(userDetail, UserDto.class);
        UserDto createdDto = service.createUser(userDto);

        CreateUserResponseModel response = mapper.map(createdDto, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
