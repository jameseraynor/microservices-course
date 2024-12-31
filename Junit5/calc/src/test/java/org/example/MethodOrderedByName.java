package org.example;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Demonstrates JUnit 5 test method ordering using MethodOrderer.MethodName
 * Tests will be executed in alphabetical order based on method names
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class MethodOrderedByName {

  /**
   * Test method that will execute third in alphabetical order
   */
  @Test
  void testC() {
    System.out.println("Running test C");
  }

  /**
   * Test method that will execute first in alphabetical order
   */
  @Test
  void testA() {
    System.out.println("Running test A");
  }

  /**
   * Test method that will execute fourth in alphabetical order
   */
  @Test
  void testD() {
    System.out.println("Running test D");
  }

  /**
   * Test method that will execute second in alphabetical order
   */
  @Test
  void testB() {
    System.out.println("Running test B");
  }
}

