package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Calculator operations Contains unit tests for basic arithmetic operations
 * (addition, subtraction, multiplication, division) Uses JUnit 5 lifecycle annotations for test
 * setup and cleanup
 */
@DisplayName("Test Calc class")
class CalcTest {

  Calc calc;

  /** Executes once before all test methods Prints initial setup message for test suite */
  @BeforeAll
  static void setup() {
    System.out.println("Testing Calc class");
  }

  /** Executes once after all test methods Prints completion message for test suite */
  @AfterAll
  static void cleanup() {
    System.out.println("Testing complete");
  }

  /**
   * Executes before each test method Creates a new Calculator instance to ensure clean state for
   * each test
   */
  @BeforeEach
  void beforeEachTestMethod() {
    System.out.println("Running @BeforeEach test...");
    calc = new Calc();
  }

  /** Executes after each test method Performs cleanup after individual test execution */
  @AfterEach
  void afterEachTestMethod() {
    System.out.println("Running @AfterEach test...");
  }

  /**
   * Tests division by zero exception handling
   *
   * <p>Purpose: Verify that calculator properly handles division by zero Test Strategy: 1. Attempt
   * to divide 4 by 0 2. Verify ArithmeticException is thrown 3. Validate the exception message
   * matches expected
   *
   * <p>Note: Currently disabled pending implementation review
   */
  @Disabled("TODO: Test is currently disabled")
  @DisplayName("Test division by zero")
  @Test
  void test_Calculator_DivideByZero_ThrowsArithmeticException() {
    System.out.println("Testing division by zero");

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
            "Division by zero should have thrown an ArithmeticException");

    // Assert - Verify exception message
    assertEquals(
        expectedExceptionMessage, actualException.getMessage(), "Unexpected exception message");
  }

  /**
   * Tests basic division operation
   *
   * <p>Purpose: Verify correct integer division Test Strategy: 1. Divide 10 by 5 2. Verify result
   * equals 2
   */
  @DisplayName("Test 10/5=2")
  @Test
  void test_Calculator_WhenFourIsDividedByTwo_ShouldReturnTwo() {
    System.out.println("Testing integer division");
    int actualResult = calc.division(10, 5);
    assertEquals(2, actualResult, "Division of 10 by 5 should equal 2");
  }

  /**
   * Tests multiplication operation
   *
   * <p>Purpose: Verify correct multiplication Test Strategy: 1. Multiply 10 by 5 2. Verify result
   * equals 50
   */
  @DisplayName("Test 10*5=50")
  @ParameterizedTest
//  @CsvSource({"10,5,50", "100,5,500", "1000,5,5000"})
  @CsvFileSource(resources = "/integerMultiplicationInputParameters.csv", numLinesToSkip = 1)
  void test_Calculator_MultiplyTenByFive_ReturnsFifty(int first, int second, int expected) {
    System.out.println("Testing multiplication");
    int result = calc.multiplication(first, second);
    assertEquals(
        expected,
        result,
        "Multiplication of " + first + " from " + second + " should equal " + expected);
  }

  /**
   * Tests addition operation
   *
   * <p>Purpose: Verify correct addition Test Strategy: 1. Add 10 and 5 2. Verify result equals 15
   */
  @DisplayName("Test 10+5=15")
  @ParameterizedTest
  @MethodSource("integerAdditionInputParameters")
  void test_Calculator_AddTenAndFive_ReturnsFifteen(int first, int second, int expected) {
    System.out.println("Testing addition");
    int result = calc.addition(first, second);
    assertEquals(
        expected, result, "Addition of " + first + " from " + second + " should equal " + expected);
  }

  /**
   * Tests subtraction operation
   *
   * <p>Purpose: Verify correct subtraction Test Strategy: 1. Subtract 5 from 10 2. Verify result
   * equals 5
   *
   * <p>Implementation follows Arrange-Act-Assert pattern: - Arrange: Set up test inputs - Act:
   * Perform subtraction - Assert: Verify result
   */
  @DisplayName("Test 10-5=5")
  @ParameterizedTest
  @MethodSource()
  void test_Calculator_SubtractFiveFromTen_ReturnsFive(int minuend, int subtrahend, int expected) {
    System.out.println("Testing subtraction");

    // Arrange - Setting up the test data

    // Act - Performing the subtraction
    int actualResult = calc.subtraction(minuend, subtrahend);

    // Assert - Verifying the result
    assertEquals(
        expected,
        actualResult,
        () -> "Subtraction of " + subtrahend + " from " + minuend + " should equal " + expected);
  }

  private static Stream<Arguments> test_Calculator_SubtractFiveFromTen_ReturnsFive() {
    return Stream.of(
        Arguments.of(10, 5, 5), Arguments.of(100, 50, 50), Arguments.of(1000, 500, 500));
  }

  private static Stream<Arguments> integerAdditionInputParameters() {
    return Stream.of(
        Arguments.of(10, 5, 15), Arguments.of(100, 50, 150), Arguments.of(1000, 500, 1500));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Radical", "Academy", "Dev"})
  void valueSourceDemonstration(String input) {
    System.out.println(input);
    assertNotNull(input);
  }

}
