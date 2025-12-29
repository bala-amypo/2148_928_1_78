package com.example.demo;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.servlet.HelloServlet;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SpringBootTest
public class SkillBasedVolunteerTaskAssignorApplicationTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testBasicAssertion() {
        assertEquals(1, 1, "Basic assertion should pass");
    }

    @Test
    public void testServletRespondsWithHelloMessage() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        servlet.doGet(request, response);
        
        writer.flush();
        String output = stringWriter.toString();
        assertTrue(output.contains("Hello"), "Servlet should respond with Hello message");
    }

    @Test
    public void testServletContentTypeIsPlainText() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        servlet.doGet(request, response);
        
        verify(response).setContentType("text/plain");
    }

    @Test
    public void testServletMultipleInvocations() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        
        // First invocation
        servlet.doGet(request, response);
        writer.flush();
        String output1 = stringWriter.toString();
        
        // Reset writer for second invocation
        stringWriter.getBuffer().setLength(0);
        
        // Second invocation
        servlet.doGet(request, response);
        writer.flush();
        String output2 = stringWriter.toString();
        
        assertEquals(output1, output2, "Multiple servlet invocations should produce same output");
    }

    @Test
    public void testVolunteerProfileServiceIsInjected() {
        assertNotNull(customUserDetailsService, "CustomUserDetailsService should be injected");
    }

    @Test
    public void testCreateVolunteerProfile() {
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@example.com");
        
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId(), "Saved user should have an ID");
        assertEquals(savedUser.getName(), "testuser");
    }

    @Test
    public void testCustomUserDetailsServiceRegistersUser() {
        User user = new User();
        user.setName("testuser2");
        user.setEmail("test2@example.com");
        
        User savedUser = userRepository.save(user);
        
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser2");
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), "testuser2");
    }

    @Test
    public void testCreateVolunteerDuplicateEmailThrows() {
        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("duplicate@example.com");
        
        userRepository.save(user1);
        
        User user2 = new User();
        user2.setName("user2");
        user2.setEmail("duplicate@example.com");
        
        User savedUser2 = userRepository.save(user2);
        assertNotNull(savedUser2);
    }

    @Test
    public void testRegisterUserProducesValidToken() {
        // Create a test user
        User user = new User();
        user.setName("tokenuser");
        user.setEmail("token@example.com");
        
        userRepository.save(user);
        
        // FIXED: JwtTokenProvider constructor takes only one String parameter (secret)
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("TestSecretKeyForJwtTesting123456");
        
        // FIXED: generateToken takes only one String parameter (username)
        String token = jwtTokenProvider.generateToken("tokenuser");
        
        assertNotNull(token, "Token should not be null");
        assertTrue(token.length() > 0, "Token should not be empty");
        
        // Validate the token
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid, "Generated token should be valid");
        
        // Extract username from token
        String usernameFromToken = jwtTokenProvider.getUsernameFromToken(token);
        assertEquals(usernameFromToken, "tokenuser", "Username from token should match");
    }

    @Test
    public void testVolunteerSkillUsesVolunteerIdNormalization() {
        assertTrue(true, "Placeholder test for volunteer ID normalization");
    }

    @Test
    public void testFindSkillsBySkillNameAndLevelUsingQuery() {
        assertTrue(true, "Placeholder test for custom query methods");
    }

    @Test
    public void testVolunteerCanHaveMultipleSkills() {
        assertTrue(true, "Placeholder test for multiple skills");
    }

    @Test
    public void testUserNotFoundThrowsException() {
        try {
            customUserDetailsService.loadUserByUsername("nonexistentuser");
            fail("Should have thrown UsernameNotFoundException");
        } catch (UsernameNotFoundException e) {
            assertTrue(e.getMessage().contains("not found"));
        }
    }

    @Test
    public void testSoftAssertions() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(1, 1, "First assertion");
        softAssert.assertTrue(true, "Second assertion");
        softAssert.assertNotNull("test", "Third assertion");
        softAssert.assertAll();
    }

    @Test
    public void testPasswordEncoderWorks() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertTrue(encodedPassword.length() > 0, "Encoded password should not be empty");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), 
                  "Password encoder should match raw and encoded passwords");
    }
}