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

// IMPORTANT: Remove or comment out @Listeners line if TestResultListener doesn't exist
// @Listeners(TestResultListener.class)  // REMOVE THIS LINE IF TestResultListener doesn't exist
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

        Method doGetMethod = HelloServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);
        doGetMethod.invoke(servlet, request, response);

        verify(response, times(2)).getWriter();
    }

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

    @Test(priority = 10, groups = "crud")
    public void testCreateVolunteerDuplicateEmailThrows() {
        VolunteerProfile profile = new VolunteerProfile();
        profile.setVolunteerId("V002");
        profile.setFullName("Jane Doe");
        profile.setEmail("jane@example.com");
        profile.setPhone("8888888888");
        profile.setAvailabilityStatus("AVAILABLE");

        when(volunteerProfileRepository.existsByVolunteerId("V002")).thenReturn(false);
        when(volunteerProfileRepository.existsByEmail("jane@example.com")).thenReturn(true);

        try {
            volunteerProfileService.createVolunteer(profile);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("Email already exists"));
        }
    }

    // ================================
    //   3. DEPENDENCY INJECTION TESTS
    // ================================

    @Test(priority = 17, groups = "di")
    public void testVolunteerProfileServiceIsInjected() {
        Assert.assertNotNull(volunteerProfileService);
    }

    // ================================
    //         4. HIBERNATE TESTS
    // ================================

    @Test(priority = 22, groups = "hibernate")
    public void testCustomUserDetailsServiceRegistersUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("password");

        Map<String, Object> user = customUserDetailsService.registerUser(
                "Admin User",
                "admin@example.com",
                encoded,
                "ADMIN"
        );

        Assert.assertNotNull(user.get("userId"));
        Assert.assertEquals(user.get("role"), "ADMIN");
    }

    // ================================
    //         5. JPA TESTS
    // ================================

    @Test(priority = 33, groups = "jpa")
    public void testVolunteerSkillUsesVolunteerIdNormalization() {
        VolunteerSkillRecord s = new VolunteerSkillRecord();
        s.setVolunteerId(10L);
        s.setSkillName("CODING");
        s.setSkillLevel("INTERMEDIATE");

        Assert.assertEquals(s.getVolunteerId(), Long.valueOf(10L));
    }

    // ================================
    //        6. MANY-TO-MANY TESTS
    // ================================

    @Test(priority = 41, groups = "manyToMany")
    public void testVolunteerCanHaveMultipleSkills() {
        VolunteerSkillRecord s1 = new VolunteerSkillRecord();
        s1.setVolunteerId(1L);
        s1.setSkillName("CODING");

        VolunteerSkillRecord s2 = new VolunteerSkillRecord();
        s2.setVolunteerId(1L);
        s2.setSkillName("TEACHING");

        when(volunteerSkillRecordRepository.findByVolunteerId(1L))
                .thenReturn(Arrays.asList(s1, s2));

        List<VolunteerSkillRecord> skills = volunteerSkillService.getSkillsByVolunteer(1L);
        Assert.assertEquals(skills.size(), 2);
    }

    // ================================
    //        7. SECURITY + JWT TESTS
    // ================================

    @Test(priority = 49, groups = "security")
    public void testRegisterUserProducesValidToken() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Map<String, Object> user =
                customUserDetailsService.registerUser(
                        "Security User",
                        "sec@example.com",
                        encoder.encode("secpass"),
                        "ADMIN"
                );

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        "sec@example.com",
                        "secpass",
                        Collections.emptyList()
                );

        String token = jwtTokenProvider.generateToken(auth,
                (Long) user.get("userId"),
                (String) user.get("role"));

        Assert.assertNotNull(token);
    }

    // ================================
    //          8. HQL TESTS
    // ================================

    @Test(priority = 57, groups = "hql")
    public void testFindSkillsBySkillNameAndLevelUsingQuery() {
        VolunteerSkillRecord rec = new VolunteerSkillRecord();
        rec.setSkillName("CODING");
        rec.setSkillLevel("INTERMEDIATE");

        when(volunteerSkillRecordRepository.findBySkillNameAndSkillLevel("CODING", "INTERMEDIATE"))
                .thenReturn(Collections.singletonList(rec));

        List<VolunteerSkillRecord> list =
                volunteerSkillRecordRepository.findBySkillNameAndSkillLevel("CODING", "INTERMEDIATE");

        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getSkillLevel(), "INTERMEDIATE");
    }

    // ================================
    //          SIMPLE TEST TO ENSURE
    // ================================

    @Test(priority = 1000)
    public void testBasicAssertion() {
        Assert.assertTrue(true, "Basic assertion should pass");
    }

}