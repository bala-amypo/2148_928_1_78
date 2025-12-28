package com.example.demo;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class SimpleTest {
    
    @Test
    public void testSimple() {
        System.out.println("=== SIMPLE TEST RUNNING ===");
        assertTrue(true, "Simple test should pass");
    }
    
    @Test
    public void testAddition() {
        System.out.println("=== ADDITION TEST RUNNING ===");
        assertTrue(2 + 2 == 4, "2+2 should be 4");
    }
}


