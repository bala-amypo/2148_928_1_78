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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        servlet.doGet(request, response);

        Assert.assertTrue(writer.toString().contains("Hello from HelloServlet"));
    }

    @Test(priority = 2, groups = "servlet")
    public void testServletContentTypeIsPlainText() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doGet(request, response);

        verify(response).setContentType("text/plain");
    }

    @Test(priority = 3, groups = "servlet")
    public void testServletMultipleInvocations() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doGet(request, response);
        servlet.doGet(request, response);

        verify(response, times(2)).getWriter();
    }

    // Add more test methods as needed...
}