package com.apicourse.photapp.api.users.photo_api_users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.apicourse.photapp.api.users.photo_api_users.shared.UserDto;

public interface UserService extends UserDetailsService{

        UserDto createUser(UserDto userDetail);
        UserDto getUserDetailsByEmail(String email);
        UserDto getUserByUserId(String userId);
}
