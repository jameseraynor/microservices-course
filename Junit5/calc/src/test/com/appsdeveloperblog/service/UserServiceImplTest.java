package com.appsdeveloperblog.service;

import org.example.io.UsersDatabase;
import org.example.io.UsersDatabaseMapImpl;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for UserServiceImpl that verifies user management functionality
 * This class uses JUnit 5 and follows a per-class lifecycle
 * Tests are executed in a specific order using @TestMethodOrder
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {

  /** Database instance for managing user data during tests */
  UsersDatabase usersDatabase;

  /** Service instance under test */
  UserService userService;

  /** Stores the user ID for test cases */
  String userId = "";

  /**
   * Initializes the test environment before all tests
   * Creates and initializes the database
   * Sets up the UserService with the database instance
   */
  @BeforeAll
  void setup() {
    // Create & initialize database
    usersDatabase = new UsersDatabaseMapImpl();
    usersDatabase.init();
    userService = new UserServiceImpl(usersDatabase);
  }

  /**
   * Cleans up resources after all tests have completed
   * Closes database connections and performs necessary cleanup
   */
  @AfterAll
  void cleanup() {
    // Close connection
    // Delete database
    usersDatabase.close();
  }

  /**
   * Tests the user creation functionality
   * Verifies that creating a new user works as expected
   *
   * @Order(1) Ensures this test runs first as other tests depend on user creation
   */
  @Test
  @Order(1)
  @DisplayName("Create User works")
  void testCreateUser_whenProvidedWithValidUserDetails_returnsUserId() {
    // Arrange
    Map<String, String> userDetails = new HashMap<>();
    userDetails.put("firstName", "Sergey");
    userDetails.put("lastName", "Kargopolov");

    // Act
    userId = userService.createUser(userDetails);

    // Assert
    Assertions.assertNotNull(userId, "User id should not be null");
    Assertions.assertTrue(userId.length() > 0, "User id should be greater than 0 characters");
  }

  /**
   * Tests the user update functionality
   * Verifies that updating an existing user's details works correctly
   *
   * @Order(2) Runs after user creation to ensure there's a user to update
   */
  @Test
  @Order(2)
  @DisplayName("Update user works")
  void testUpdateUser_whenProvidedWithValidUserDetails_returnsUpdatedUserDetails() {
    // Arrange
    Map<String, String> newUserDetails = new HashMap<>();
    newUserDetails.put("firstName", "John");
    newUserDetails.put("lastName", "Doe");

    // Act
    Map userDetails = userService.updateUser(userId, newUserDetails);

    // Assert
    Assertions.assertEquals(
            newUserDetails.get("firstName"),
            userDetails.get("firstName"),
            "First name must be equal"
    );
    Assertions.assertEquals(
            newUserDetails.get("lastName"),
            userDetails.get("lastName"),
            "Last name must be equal"
    );
  }

  /**
   * Tests the user retrieval functionality
   * Verifies that getting user details with a valid ID returns correct information
   *
   * @Order(3) Runs after update to verify user details can be retrieved
   */
  @Test
  @Order(3)
  @DisplayName("Find user works")
  void testGetUserDetails_whenProvidedWithValidUserId_returnsUserDetails() {
    // Act
    Map userDetails = userService.getUserDetails(userId);

    // Assert
    Assertions.assertNotNull(userDetails, "User Details Should not be null");
    Assertions.assertEquals(userId, userDetails.get("userId"), "Incorrect user Id");
  }

  /**
   * Tests the user deletion functionality
   * Verifies that deleting a user removes them from the system
   *
   * @Order(4) Runs last as it removes the test user from the system
   */
  @Test
  @Order(4)
  @DisplayName("Delete user works")
  void testDeleteUser_whenProvidedWithValidUserId_returnsUserDetails() {
    // Act
    userService.deleteUser(userId);

    // Assert
    Assertions.assertNull(userService.getUserDetails(userId));
  }
}
