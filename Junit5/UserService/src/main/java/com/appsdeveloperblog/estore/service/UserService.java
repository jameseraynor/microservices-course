package com.appsdeveloperblog.estore.service;

import com.appsdeveloperblog.estore.model.User;

public interface UserService {

    User registerUser(String firstName, String lastName, String email, String password, String repeatPassword);
}

