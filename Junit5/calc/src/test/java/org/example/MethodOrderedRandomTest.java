package org.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Demonstrates JUnit 5 test method ordering using Random execution order
 * Tests will be executed in a different random order each time the test suite runs
 */
@TestMethodOrder(MethodOrderer.Random.class)
public class MethodOrderedRandomTest {

    /**
     * First test method that prints its execution
     * The actual execution order will be random
     */
    @Test
    public void testMethod1() {
        System.out.println("Running testMethod1");
    }

    /**
     * Second test method that prints its execution
     * The actual execution order will be random
     */
    @Test
    public void testMethod2() {
        System.out.println("Running testMethod2");
    }

    /**
     * Third test method that prints its execution
     * The actual execution order will be random
     */
    @Test
    public void testMethod3() {
        System.out.println("Running testMethod3");
    }

    /**
     * Fourth test method that prints its execution
     * The actual execution order will be random
     */
    @Test
    public void testMethod4() {
        System.out.println("Running testMethod4");
    }
}
