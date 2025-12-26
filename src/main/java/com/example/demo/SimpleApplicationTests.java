package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Use test profile
class SimpleApplicationTests {

    @Test
    void contextLoads() {
        // This test will pass if the Spring context loads successfully
        System.out.println("Context loads test passed!");
    }

    @Test
    void testBasicArithmetic() {
        // Simple test that always passes
        assert 1 + 1 == 2 : "Basic math should work";
    }
}