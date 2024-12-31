package com.appsdeveloperblog.estore.service;

import com.appsdeveloperblog.estore.data.UserRepository;
import com.appsdeveloperblog.estore.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks UserServiceImpl userService;
  @Mock UserRepository userRepository;
  @Mock EmailVerificationServiceImpl emailVerificationService;

  String firstName = "XXXXXXXXXXXXXX";
  String lastName = "XXXXXXXXXXXXXX";
  String email = "XXXXXXXXXXXXXX";
  String password = "XXXXXXXXXXXXXX";
  String repeatPassword = "XXXXXXXXXXXXXX";

  @BeforeEach
  public void init() {
    firstName = "XXXXXXXXXXXXXX";
    lastName = "XXXXXXXXXXXXXX";
    email = "XXXXXXXXXXXXXX";
    password = "XXXXXXXXXXXXXX";
    repeatPassword = "XXXXXXXXXXXXXX";
  }

  @Test()
  public void testCreateUser_WhenUserDetailsProvided_returnUserObject() {
    // Arrange
    when(userRepository.save(any(User.class))).thenReturn(true);

    // Act
    User user = userService.registerUser(firstName, lastName, email, password, repeatPassword);

    // Assert
    assertNotNull(user, "The user should not be null");
    assertEquals(firstName, user.getFirstName(), "User first name is incorrect");
    assertEquals(lastName, user.getLastName(), "User last name is incorrect");
    assertEquals(email, user.getEmail(), "User email is incorrect");
    assertNotNull(user.getId(), "User id is incorrect");

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  public void testCreateUser_WhenFirstNameEmpty_throwsException() {
    // Arrange
    String firstName = "";
    String expectedExceptionMessage = "User's first name is empty";

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.registerUser(firstName, lastName, email, password, repeatPassword);
            },
            "First Name is empty and should throw error");

    // Assert
    assertEquals(
        expectedExceptionMessage, exception.getMessage(), "Exception error message is not correct");
  }

  @Test
  public void testCreateUser_WhenLastNameEmpty_throwsException() {
    // Arrange
    String lastName = "";
    String expectedExceptionMessage = "User's last name is empty";

    // Act & Assert
    RuntimeException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              userService.registerUser(firstName, lastName, email, password, repeatPassword);
            },
            "Last Name is empty and should throw error");

    // Assert
    assertEquals(
        expectedExceptionMessage, exception.getMessage(), "Exception error message is not correct");
  }

  @Test
  @DisplayName("If save() method causes RuntimeException, a UserServiceException is thrown")
  public void testCreateUser_WhenSaveMethodThrowsException_thenThrowsRuntimeException() {
    // Arrange
    when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);

    // Act & Assert
    RuntimeException runtimeException =
        assertThrows(
            UserServiceException.class,
            () -> {
              userService.registerUser(firstName, lastName, email, password, repeatPassword);
            },
            "save() method from UserRepository class should throw RuntimeException");

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName(
      "EmailNotificationException is handled because we are catching it and converting it to a RuntimeException")
  public void
      TestCreateUser_WhenEmailNotificationExceptionThrown_CaughtAndReThrownAsRuntimeException() {
    // Arrange
    when(userRepository.save(any(User.class))).thenReturn(true);
    doThrow(EmailNotificationException.class)
        .when(emailVerificationService)
        .scheduleEmailConfirmation(any(User.class));

    // Act & Assert
    assertThrows(
        UserServiceException.class,
        () -> {
          userService.registerUser(firstName, lastName, email, password, repeatPassword);
        },
        "Should have thrown UserServiceException instead");

    // Assert
    verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
  }

  @Test
  @DisplayName("Schedule Email Confirmation is called exactly once")
  public void testCreateUser_WhenUserCreated_ScheduleEmailConfirmation() {
    // Arrange
    when(userRepository.save(any(User.class))).thenReturn(true);
    doCallRealMethod().when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

    // Act
    userService.registerUser(firstName, lastName, email, password, repeatPassword);

    // Assert
    verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
  }

}
