package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Test class for Calculator that verifies arithmetic operations
 * Specifically focuses on testing division by zero exception handling
 */
public class RepeatedCalcTest {

  /** Calculator instance to be used across test methods */
  Calc calc;

  /**
   * Sets up a new Calculator instance before each test method
   * Ensures each test starts with a fresh calculator object
   */
  @BeforeEach
  void beforeEachTestMethod() {
    System.out.println("Running @BeforeEach test...");
    calc = new Calc();
  }

  /**
   * Performs cleanup operations after each test method
   * Currently only logs the completion of test
   */
  @AfterEach
  void afterEachTestMethod() {
    System.out.println("Running @AfterEach test...");
  }

  /**
   * Tests that division by zero throws appropriate ArithmeticException
   * This test is repeated 3 times to ensure consistent behavior
   *
   * @param repetitionInfo Provides information about the current repetition
   * @param testInfo Provides information about the test method
   * @throws ArithmeticException when attempting to divide by zero
   */
  @DisplayName("Test division by zero")
  @RepeatedTest(value = 3, name = "{displayName} {currentRepetition}/{totalRepetitions}")
  void test_Calculator_DivideByZero_ThrowsArithmeticException(
          RepetitionInfo repetitionInfo,
          TestInfo testInfo) {
    // Log current test execution details
    System.out.println("Testing " + testInfo.getTestMethod().get().getName());
    System.out.println("Running test #" + repetitionInfo.getCurrentRepetition()
            + " of " + repetitionInfo.getTotalRepetitions());

    // Arrange - Set up test inputs
    int dividend = 4;
    int divisor = 0;
    String expectedExceptionMessage = "/ by zero";

    // Act & Assert - Verify exception throwing
    ArithmeticException actualException =
            assertThrows(
                    ArithmeticException.class,
                    () -> {
                      calc.division(dividend, divisor);
                    },
                    "Division by zero should throw ArithmeticException"
            );

    // Additional assertion could be added here to verify the exception message
    assertEquals(expectedExceptionMessage, actualException.getMessage(),
            "Exception message should match expected");
  }
}
