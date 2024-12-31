package com.appsdeveloperblog.estore.service;

import com.appsdeveloperblog.estore.data.UserRepository;
import com.appsdeveloperblog.estore.data.UserRepositoryImpl;
import com.appsdeveloperblog.estore.model.User;

import java.util.UUID;

public class UserServiceImpl implements UserService {

  UserRepository userRepository;
  EmailVerificationService emailVerificationService;

  public UserServiceImpl() {
    this.userRepository = new UserRepositoryImpl();
  }

  public UserServiceImpl(UserRepository userRepository, EmailVerificationService  emailVerificationService) {
    this.userRepository = userRepository;
    this.emailVerificationService = emailVerificationService;
  }

  @Override
  public User registerUser(
      String firstName, String lastName, String email, String password, String repeatPassword) {

    if (firstName == null || firstName.isEmpty())
      throw new IllegalArgumentException("User's first name is empty");
    if (lastName == null || lastName.isEmpty())
      throw new IllegalArgumentException("User's last name is empty");
    User user =
        new User(
            firstName, lastName, email, password, repeatPassword, UUID.randomUUID().toString());
    boolean isSuccess;
    try {
      isSuccess = this.userRepository.save(user);
    } catch (Exception e) {
      throw new UserServiceException(e.getMessage());
    }

    if (!isSuccess) throw new UserServiceException("Could not save user");

    try{

      emailVerificationService.scheduleEmailConfirmation(user);
    } catch (RuntimeException e) {
      throw new UserServiceException(e.getMessage());
    }

    return user;
  }
}
