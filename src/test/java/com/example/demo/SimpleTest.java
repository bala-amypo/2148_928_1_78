package com.example.demo;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class SimpleTest {
    
    @Test
    public void testSimple() {
        System.out.println("Test is running!");
        assertTrue(true, "Simple test should pass");
    }
}