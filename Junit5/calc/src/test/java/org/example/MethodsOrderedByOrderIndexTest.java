package org.example;

import org.junit.jupiter.api.*;

/**
 * Demonstrates JUnit 5 test method ordering using @Order annotation
 * Tests will be executed in the order specified by the @Order value
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MethodsOrderedByOrderIndexTest {

    /** StringBuilder to track the sequence of test execution */
    StringBuilder completed = new StringBuilder("");

    /**
     * Executes after each test method
     * Prints the current sequence of completed tests
     */
    @AfterEach
    void afterEach() {
        System.out.println("completed " + completed);
    }

    /**
     * First test in execution order
     * Appends "C-" to the completed sequence
     */
    @Order(1)
    @Test
    void testC() {
        System.out.println("Running test C");
        completed.append("C-");
    }

    /**
     * Second test in execution order
     * Appends "A-" to the completed sequence
     */
    @Order(2)
    @Test
    void testA() {
        System.out.println("Running test A");
        completed.append("A-");
    }

    /**
     * Third test in execution order
     * Appends "D-" to the completed sequence
     */
    @Order(3)
    @Test
    void testD() {
        System.out.println("Running test D");
        completed.append("D-");
    }
}
