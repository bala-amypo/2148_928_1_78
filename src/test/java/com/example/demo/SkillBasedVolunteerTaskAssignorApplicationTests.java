package com.example.demo;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.servlet.HelloServlet;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.util.SkillLevelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Listeners(TestResultListener.class)
public class SkillBasedVolunteerTaskAssignorApplicationTests {

    private VolunteerProfileRepository volunteerProfileRepository;
    private VolunteerSkillRecordRepository volunteerSkillRecordRepository;
    private TaskRecordRepository taskRecordRepository;
    private TaskAssignmentRecordRepository taskAssignmentRecordRepository;
    private AssignmentEvaluationRecordRepository assignmentEvaluationRecordRepository;

    private VolunteerProfileService volunteerProfileService;
    private VolunteerSkillService volunteerSkillService;
    private TaskRecordService taskRecordService;
    private TaskAssignmentService taskAssignmentService;
    private AssignmentEvaluationService assignmentEvaluationService;

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeClass
    public void setup() {
        volunteerProfileRepository = Mockito.mock(VolunteerProfileRepository.class);
        volunteerSkillRecordRepository = Mockito.mock(VolunteerSkillRecordRepository.class);
        taskRecordRepository = Mockito.mock(TaskRecordRepository.class);
        taskAssignmentRecordRepository = Mockito.mock(TaskAssignmentRecordRepository.class);
        assignmentEvaluationRecordRepository = Mockito.mock(AssignmentEvaluationRecordRepository.class);

        volunteerProfileService = new VolunteerProfileServiceImpl(volunteerProfileRepository);
        volunteerSkillService = new VolunteerSkillServiceImpl(volunteerSkillRecordRepository);
        taskRecordService = new TaskRecordServiceImpl(taskRecordRepository);
        taskAssignmentService = new TaskAssignmentServiceImpl(
                taskAssignmentRecordRepository,
                taskRecordRepository,
                volunteerProfileRepository,
                volunteerSkillRecordRepository);
        assignmentEvaluationService = new AssignmentEvaluationServiceImpl(
                assignmentEvaluationRecordRepository,
                taskAssignmentRecordRepository);

        customUserDetailsService = new CustomUserDetailsService();
        jwtTokenProvider = new JwtTokenProvider(
                "VerySecretKeyForJwtDemoApplication123456", 3600000L);
    }

    // ================================
    //          1. SERVLET TESTS
    // ================================

    @Test(priority = 1, groups = "servlet")
    public void testServletRespondsWithHelloMessage() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Use reflection to call the protected method
        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);

        Assert.assertTrue(writer.toString().contains("Hello from HelloServlet"));
    }

    @Test(priority = 2, groups = "servlet")
    public void testServletContentTypeIsPlainText() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // Use reflection to call the protected method
        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);

        verify(response).setContentType("text/plain");
    }

    @Test(priority = 3, groups = "servlet")
    public void testServletMultipleInvocations() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // Use reflection to call the protected method
        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);
        doGetMethod.invoke(servlet, request, response);

        verify(response, times(2)).getWriter();
    }

    @Test(priority = 4, groups = "servlet")
    public void testServletHandlesNullRequestGracefully() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        // Use reflection to call the protected method
        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, null, response);
        
        verify(response).getWriter();
    }

    @Test(priority = 5, groups = "servlet")
    public void testServletDoesNotThrowOnNullResponseWriter() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenThrow(new RuntimeException("Writer error"));

        try {
            // Use reflection to call the protected method
            Method doGetMethod = HelloServlet.class.getDeclaredMethod(
                "doGet", HttpServletRequest.class, HttpServletResponse.class);
            doGetMethod.setAccessible(true);
            doGetMethod.invoke(servlet, request, response);
            Assert.fail("Expected exception");
        } catch (Exception ex) {
            // Check if the cause is our expected RuntimeException
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException) {
                Assert.assertEquals(cause.getMessage(), "Writer error");
            } else {
                throw ex;
            }
        }
    }

    @Test(priority = 6, groups = "servlet")
    public void testServletOutputLengthGreaterThanZero() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        // Use reflection to call the protected method
        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);
        
        Assert.assertTrue(writer.toString().length() > 0);
    }

    @Test(priority = 7, groups = "servlet")
    public void testServletOutputIsDeterministic() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter w1 = new StringWriter();
        StringWriter w2 = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(w1))
                                   .thenReturn(new PrintWriter(w2));

        // Use reflection to call the protected method
        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);
        doGetMethod.invoke(servlet, request, response);

        Assert.assertEquals(w1.toString(), w2.toString());
    }

    @Test(priority = 8, groups = "servlet")
    public void testServletSupportsHttpServletInheritance() {
        HelloServlet servlet = new HelloServlet();
        Assert.assertTrue(servlet instanceof jakarta.servlet.http.HttpServlet);
    }

    // Continue with the rest of your tests...
    // ================================
    //          2. CRUD TESTS
    // ================================

    @Test(priority = 9, groups = "crud")
    public void testCreateVolunteerProfile() {
        VolunteerProfile profile = new VolunteerProfile();
        profile.setVolunteerId("V001");
        profile.setFullName("John Doe");
        profile.setEmail("john@example.com");
        profile.setPhone("9999999999");
        profile.setAvailabilityStatus("AVAILABLE");

        when(volunteerProfileRepository.existsByVolunteerId("V001")).thenReturn(false);
        when(volunteerProfileRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(volunteerProfileRepository.existsByPhone("9999999999")).thenReturn(false);
        when(volunteerProfileRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        VolunteerProfile saved = volunteerProfileService.createVolunteer(profile);
        Assert.assertEquals(saved.getVolunteerId(), "V001");
    }

    // Add the rest of your test methods here...
    // Make sure to fix any other direct doGet() calls if they exist
}