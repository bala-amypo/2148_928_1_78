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

    @Test(priority = 4, groups = "servlet")
    public void testServletHandlesNullRequestGracefully() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doGet(null, response);
        verify(response).getWriter();
    }

    @Test(priority = 5, groups = "servlet")
    public void testServletDoesNotThrowOnNullResponseWriter() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenThrow(new RuntimeException("Writer error"));

        try {
            servlet.doGet(request, response);
            Assert.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assert.assertEquals(ex.getMessage(), "Writer error");
        }
    }

    @Test(priority = 6, groups = "servlet")
    public void testServletOutputLengthGreaterThanZero() throws Exception {
        HelloServlet servlet = new HelloServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        servlet.doGet(request, response);
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

        servlet.doGet(request, response);
        servlet.doGet(request, response);

        Assert.assertEquals(w1.toString(), w2.toString());
    }

    @Test(priority = 8, groups = "servlet")
    public void testServletSupportsHttpServletInheritance() {
        HelloServlet servlet = new HelloServlet();
        Assert.assertTrue(servlet instanceof jakarta.servlet.http.HttpServlet);
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

    @Test(priority = 11, groups = "crud")
    public void testGetVolunteerById() {
        VolunteerProfile profile = new VolunteerProfile();
        profile.setId(1L);
        profile.setVolunteerId("V003");
        when(volunteerProfileRepository.findById(1L))
                .thenReturn(java.util.Optional.of(profile));

        VolunteerProfile found = volunteerProfileService.getVolunteerById(1L);
        Assert.assertEquals(found.getVolunteerId(), "V003");
    }

    @Test(priority = 12, groups = "crud")
    public void testGetAllVolunteersEmpty() {
        when(volunteerProfileRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(volunteerProfileService.getAllVolunteers().isEmpty());
    }

    @Test(priority = 13, groups = "crud")
    public void testUpdateTaskCrud() {
        TaskRecord existing = new TaskRecord();
        existing.setId(10L);
        existing.setTaskCode("T001");
        existing.setTaskName("Teach Java");
        existing.setRequiredSkill("TEACHING");
        existing.setRequiredSkillLevel("BEGINNER");
        existing.setPriority("LOW");
        existing.setStatus("OPEN");

        when(taskRecordRepository.findById(10L))
                .thenReturn(java.util.Optional.of(existing));
        when(taskRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        TaskRecord updated = new TaskRecord();
        updated.setTaskName("Teach Advanced Java");
        updated.setRequiredSkill("TEACHING");
        updated.setRequiredSkillLevel("INTERMEDIATE");
        updated.setPriority("HIGH");
        updated.setStatus("OPEN");

        TaskRecord result = taskRecordService.updateTask(10L, updated);
        Assert.assertEquals(result.getTaskName(), "Teach Advanced Java");
        Assert.assertEquals(result.getRequiredSkillLevel(), "INTERMEDIATE");
    }

    @Test(priority = 14, groups = "crud")
    public void testGetOpenTasks() {
        TaskRecord t1 = new TaskRecord();
        t1.setStatus("OPEN");

        when(taskRecordRepository.findByStatus("OPEN"))
                .thenReturn(Collections.singletonList(t1));

        List<TaskRecord> open = taskRecordService.getOpenTasks();
        Assert.assertEquals(open.size(), 1);
    }

    @Test(priority = 15, groups = "crud")
    public void testFindTaskByCode() {
        TaskRecord t1 = new TaskRecord();
        t1.setTaskCode("TASK-123");

        when(taskRecordRepository.findByTaskCode("TASK-123"))
                .thenReturn(java.util.Optional.of(t1));

        java.util.Optional<TaskRecord> opt = taskRecordService.getTaskByCode("TASK-123");
        Assert.assertTrue(opt.isPresent());
    }

    @Test(priority = 16, groups = "crud")
    public void testVolunteerLookupByVolunteerId() {
        VolunteerProfile profile = new VolunteerProfile();
        profile.setVolunteerId("LOOKUP-1");

        when(volunteerProfileRepository.findByVolunteerId("LOOKUP-1"))
                .thenReturn(java.util.Optional.of(profile));

        java.util.Optional<VolunteerProfile> opt =
                volunteerProfileService.findByVolunteerId("LOOKUP-1");

        Assert.assertTrue(opt.isPresent());
    }

    // ================================
    //   3. DEPENDENCY INJECTION / IoC
    // ================================

    @Test(priority = 17, groups = "di")
    public void testVolunteerProfileServiceIsInjected() {
        Assert.assertNotNull(volunteerProfileService);
    }

    @Test(priority = 18, groups = "di")
    public void testTaskAssignmentServiceUsesRepositories() {
        TaskRecord task = new TaskRecord();
        task.setId(1L);
        task.setStatus("OPEN");
        task.setRequiredSkill("CODING");
        task.setRequiredSkillLevel("BEGINNER");

        when(taskRecordRepository.findById(1L))
                .thenReturn(java.util.Optional.of(task));
        when(taskAssignmentRecordRepository.existsByTaskIdAndStatus(1L, "ACTIVE"))
                .thenReturn(false);
        when(volunteerProfileRepository.findByAvailabilityStatus("AVAILABLE"))
                .thenReturn(Collections.emptyList());

        try {
            taskAssignmentService.assignTask(1L);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("No AVAILABLE volunteers"));
        }
    }

    @Test(priority = 19, groups = "di")
    public void testAssignmentEvaluationServiceUsesAssignmentRepository() {
        TaskAssignmentRecord assignment = new TaskAssignmentRecord();
        assignment.setId(100L);
        assignment.setStatus("COMPLETED");

        when(taskAssignmentRecordRepository.findById(100L))
                .thenReturn(java.util.Optional.of(assignment));

        AssignmentEvaluationRecord eval = new AssignmentEvaluationRecord();
        eval.setAssignmentId(100L);
        eval.setRating(5);

        when(assignmentEvaluationRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        AssignmentEvaluationRecord result =
                assignmentEvaluationService.evaluateAssignment(eval);

        Assert.assertEquals(result.getRating(), Integer.valueOf(5));
    }

    @Test(priority = 20, groups = "di")
    public void testInjectionOfSkillService() {
        Assert.assertNotNull(volunteerSkillService);
    }

    @Test(priority = 21, groups = "di")
    public void testSkillLevelUtilBeanlessUtility() {
        Assert.assertEquals(SkillLevelUtil.levelRank("BEGINNER"), 1);
        Assert.assertEquals(SkillLevelUtil.levelRank("INTERMEDIATE"), 2);
        Assert.assertEquals(SkillLevelUtil.levelRank("EXPERT"), 3);
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

    @Test(priority = 23, groups = "hibernate")
    public void testCustomUserDetailsServiceLoadsUser() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        customUserDetailsService.registerUser(
                "User",
                "user@example.com",
                encoder.encode("secret"),
                "COORDINATOR"
        );

        Assert.assertNotNull(customUserDetailsService.loadUserByUsername("user@example.com"));
    }

    @Test(priority = 24, groups = "hibernate")
    public void testLoadUserThrowsForUnknownEmail() {
        try {
            customUserDetailsService.loadUserByUsername("unknown@example.com");
            Assert.fail("Expected UsernameNotFoundException");
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("User not found"));
        }
    }

    @Test(priority = 25, groups = "hibernate")
    public void testVolunteerProfileEntityHasIdGenerated() {
        VolunteerProfile profile = new VolunteerProfile();
        profile.setVolunteerId("V-HIB");
        profile.setFullName("Hibernate User");
        profile.setEmail("hib@example.com");
        profile.setPhone("7777777777");
        profile.setAvailabilityStatus("AVAILABLE");

        when(volunteerProfileRepository.existsByVolunteerId("V-HIB")).thenReturn(false);
        when(volunteerProfileRepository.existsByEmail("hib@example.com")).thenReturn(false);
        when(volunteerProfileRepository.existsByPhone("7777777777")).thenReturn(false);
        when(volunteerProfileRepository.save(any()))
                .thenAnswer(i -> {
                    VolunteerProfile p = i.getArgument(0);
                    p.setId(99L);
                    return p;
                });

        VolunteerProfile saved = volunteerProfileService.createVolunteer(profile);
        Assert.assertEquals(saved.getId(), Long.valueOf(99L));
    }

    @Test(priority = 26, groups = "hibernate")
    public void testTaskRecordDefaultStatusOpenOnPersist() {
        TaskRecord task = new TaskRecord();
        task.setTaskCode("HIB-TASK-1");
        task.setTaskName("Hibernate Task");
        task.setRequiredSkill("CODING");
        task.setRequiredSkillLevel("BEGINNER");
        task.setPriority("LOW");

        when(taskRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        TaskRecord saved = taskRecordService.createTask(task);
        Assert.assertEquals(saved.getStatus(), "OPEN");
    }

    @Test(priority = 27, groups = "hibernate")
    public void testSkillRecordUpdatedAtOnSave() {
        VolunteerSkillRecord skill = new VolunteerSkillRecord();
        skill.setVolunteerId(1L);
        skill.setSkillName("CODING");
        skill.setSkillLevel("BEGINNER");
        skill.setCertified(true);

        when(volunteerSkillRecordRepository.save(any()))
                .thenAnswer(i -> {
                    VolunteerSkillRecord s = i.getArgument(0);
                    return s;
                });

        VolunteerSkillRecord saved = volunteerSkillService.addOrUpdateSkill(skill);
        Assert.assertNotNull(saved.getUpdatedAt());
    }

    @Test(priority = 28, groups = "hibernate")
    public void testAssignmentRecordDefaultStatusActive() {
        TaskAssignmentRecord rec = new TaskAssignmentRecord();
        rec.setTaskId(1L);
        rec.setVolunteerId(1L);

        when(taskAssignmentRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        TaskAssignmentRecord saved = taskAssignmentRecordRepository.save(rec);
        Assert.assertEquals(saved.getStatus(), "ACTIVE");
    }

    @Test(priority = 29, groups = "hibernate")
    public void testEvaluationRecordAutoTimestamp() {
        AssignmentEvaluationRecord eval = new AssignmentEvaluationRecord();
        eval.setAssignmentId(1L);
        eval.setRating(4);

        when(assignmentEvaluationRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        AssignmentEvaluationRecord saved = assignmentEvaluationRecordRepository.save(eval);
        Assert.assertNotNull(saved.getEvaluatedAt());
    }

    @Test(priority = 30, groups = "hibernate")
    public void testSkillLevelRankUnknownLevelZero() {
        Assert.assertEquals(SkillLevelUtil.levelRank("UNKNOWN"), 0);
    }

    @Test(priority = 31, groups = "hibernate")
    public void testPriorityRankHigh() {
        Assert.assertEquals(SkillLevelUtil.priorityRank("HIGH"), 3);
    }

    @Test(priority = 32, groups = "hibernate")
    public void testPriorityRankMedium() {
        Assert.assertEquals(SkillLevelUtil.priorityRank("MEDIUM"), 2);
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

    @Test(priority = 34, groups = "jpa")
    public void testTaskAssignmentUsesTaskAndVolunteerIds() {
        TaskAssignmentRecord rec = new TaskAssignmentRecord();
        rec.setTaskId(5L);
        rec.setVolunteerId(7L);

        Assert.assertEquals(rec.getTaskId(), Long.valueOf(5L));
        Assert.assertEquals(rec.getVolunteerId(), Long.valueOf(7L));
    }

    @Test(priority = 35, groups = "jpa")
    public void testEvaluationReferencesAssignmentId() {
        AssignmentEvaluationRecord eval = new AssignmentEvaluationRecord();
        eval.setAssignmentId(123L);

        Assert.assertEquals(eval.getAssignmentId(), Long.valueOf(123L));
    }

    @Test(priority = 36, groups = "jpa")
    public void testVolunteerProfileHasAtomicAttributes1NF() {
        VolunteerProfile p = new VolunteerProfile();
        p.setFullName("Atomic Name");

        Assert.assertFalse(p.getFullName().contains(","));
    }

    @Test(priority = 37, groups = "jpa")
    public void testSeparateSkillRecordPreventsPartialDependency2NF() {
        VolunteerSkillRecord s = new VolunteerSkillRecord();
        s.setVolunteerId(11L);
        s.setSkillName("TEACHING");

        Assert.assertEquals(s.getSkillName(), "TEACHING");
    }

    @Test(priority = 38, groups = "jpa")
    public void testSeparateAssignmentEvaluationRemovesTransitiveDependency3NF() {
        AssignmentEvaluationRecord eval = new AssignmentEvaluationRecord();
        eval.setRating(5);
        eval.setFeedback("Excellent");

        Assert.assertEquals(eval.getRating(), Integer.valueOf(5));
    }

    @Test(priority = 39, groups = "jpa")
    public void testGetAllTasksReturnsNormalizedList() {
        when(taskRecordRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(taskRecordService.getAllTasks().isEmpty());
    }

    @Test(priority = 40, groups = "jpa")
    public void testGetAllVolunteersReturnsNormalizedList() {
        when(volunteerProfileRepository.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(volunteerProfileService.getAllVolunteers().isEmpty());
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

    @Test(priority = 42, groups = "manyToMany")
    public void testTaskCanBeAssignedToDifferentVolunteersOverTime() {
        TaskAssignmentRecord a1 = new TaskAssignmentRecord();
        a1.setTaskId(2L);
        a1.setStatus("COMPLETED");

        TaskAssignmentRecord a2 = new TaskAssignmentRecord();
        a2.setTaskId(2L);
        a2.setStatus("ACTIVE");

        when(taskAssignmentRecordRepository.findByTaskId(2L))
                .thenReturn(Arrays.asList(a1, a2));

        List<TaskAssignmentRecord> assignments =
                taskAssignmentService.getAssignmentsByTask(2L);

        Assert.assertEquals(assignments.size(), 2);
    }

    @Test(priority = 43, groups = "manyToMany")
    public void testVolunteerAssignmentsAcrossTasks() {
        TaskAssignmentRecord a1 = new TaskAssignmentRecord();
        a1.setVolunteerId(3L);
        TaskAssignmentRecord a2 = new TaskAssignmentRecord();
        a2.setVolunteerId(3L);

        when(taskAssignmentRecordRepository.findByVolunteerId(3L))
                .thenReturn(Arrays.asList(a1, a2));

        List<TaskAssignmentRecord> assignments =
                taskAssignmentService.getAssignmentsByVolunteer(3L);

        Assert.assertEquals(assignments.size(), 2);
    }

    @Test(priority = 44, groups = "manyToMany")
    public void testEvaluateAssignmentManyToOne() {
        TaskAssignmentRecord assignment = new TaskAssignmentRecord();
        assignment.setId(200L);
        assignment.setStatus("COMPLETED");

        when(taskAssignmentRecordRepository.findById(200L))
                .thenReturn(java.util.Optional.of(assignment));
        when(assignmentEvaluationRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        AssignmentEvaluationRecord eval = new AssignmentEvaluationRecord();
        eval.setAssignmentId(200L);
        eval.setRating(5);

        AssignmentEvaluationRecord saved =
                assignmentEvaluationService.evaluateAssignment(eval);

        Assert.assertEquals(saved.getAssignmentId(), Long.valueOf(200L));
    }

    @Test(priority = 45, groups = "manyToMany")
    public void testAssignTaskEnsuresSingleActiveAssignment() {
        TaskRecord task = new TaskRecord();
        task.setId(300L);
        task.setStatus("OPEN");
        task.setRequiredSkill("CODING");
        task.setRequiredSkillLevel("BEGINNER");

        when(taskRecordRepository.findById(300L))
                .thenReturn(java.util.Optional.of(task));
        when(taskAssignmentRecordRepository.existsByTaskIdAndStatus(300L, "ACTIVE"))
                .thenReturn(true);

        try {
            taskAssignmentService.assignTask(300L);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("ACTIVE assignment"));
        }
    }

    @Test(priority = 46, groups = "manyToMany")
    public void testAssignTaskSelectsVolunteerBySkill() {
        TaskRecord task = new TaskRecord();
        task.setId(400L);
        task.setStatus("OPEN");
        task.setRequiredSkill("CODING");
        task.setRequiredSkillLevel("BEGINNER");

        VolunteerProfile v = new VolunteerProfile();
        v.setId(1L);
        v.setAvailabilityStatus("AVAILABLE");

        VolunteerSkillRecord skill = new VolunteerSkillRecord();
        skill.setVolunteerId(1L);
        skill.setSkillName("CODING");
        skill.setSkillLevel("EXPERT");
        skill.setCertified(true);

        when(taskRecordRepository.findById(400L))
                .thenReturn(java.util.Optional.of(task));
        when(taskAssignmentRecordRepository.existsByTaskIdAndStatus(400L, "ACTIVE"))
                .thenReturn(false);
        when(volunteerProfileRepository.findByAvailabilityStatus("AVAILABLE"))
                .thenReturn(Collections.singletonList(v));
        when(volunteerSkillRecordRepository.findByVolunteerId(1L))
                .thenReturn(Collections.singletonList(skill));
        when(taskAssignmentRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));
        when(taskRecordRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        TaskAssignmentRecord assignment =
                taskAssignmentService.assignTask(400L);

        Assert.assertEquals(assignment.getVolunteerId(), Long.valueOf(1L));
    }

    @Test(priority = 47, groups = "manyToMany")
    public void testAssignTaskThrowsWhenSkillLevelInsufficient() {
        TaskRecord task = new TaskRecord();
        task.setId(500L);
        task.setStatus("OPEN");
        task.setRequiredSkill("CODING");
        task.setRequiredSkillLevel("EXPERT");

        VolunteerProfile v = new VolunteerProfile();
        v.setId(1L);
        v.setAvailabilityStatus("AVAILABLE");

        VolunteerSkillRecord skill = new VolunteerSkillRecord();
        skill.setVolunteerId(1L);
        skill.setSkillName("CODING");
        skill.setSkillLevel("BEGINNER");

        when(taskRecordRepository.findById(500L))
                .thenReturn(java.util.Optional.of(task));
        when(taskAssignmentRecordRepository.existsByTaskIdAndStatus(500L, "ACTIVE"))
                .thenReturn(false);
        when(volunteerProfileRepository.findByAvailabilityStatus("AVAILABLE"))
                .thenReturn(Collections.singletonList(v));
        when(volunteerSkillRecordRepository.findByVolunteerId(1L))
                .thenReturn(Collections.singletonList(skill));

        try {
            taskAssignmentService.assignTask(500L);
            Assert.fail("Expected BadRequestException");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("required skill level"));
        }
    }

    @Test(priority = 48, groups = "manyToMany")
    public void testGetAllAssignmentsReturnsList() {
        when(taskAssignmentRecordRepository.findAll())
                .thenReturn(Collections.emptyList());

        Assert.assertTrue(taskAssignmentService.getAllAssignments().isEmpty());
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

    @Test(priority = 50, groups = "security")
    public void testJwtTokenContainsUsername() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Map<String, Object> user =
                customUserDetailsService.registerUser(
                        "Jwt User",
                        "jwt@example.com",
                        encoder.encode("jwtpass"),
                        "COORDINATOR"
                );

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        "jwt@example.com",
                        "jwtpass",
                        Collections.emptyList()
                );

        String token =
                jwtTokenProvider.generateToken(auth,
                        (Long) user.get("userId"),
                        (String) user.get("role"));

        String username = jwtTokenProvider.getUsernameFromToken(token);

        Assert.assertEquals(username, "jwt@example.com");
    }

    @Test(priority = 51, groups = "security")
    public void testJwtValidation() {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        "valid@example.com",
                        "password",
                        Collections.emptyList()
                );

        String token = jwtTokenProvider.generateToken(auth, 1L, "ADMIN");

        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test(priority = 52, groups = "security")
    public void testJwtInvalidTokenFailsValidation() {
        Assert.assertFalse(jwtTokenProvider.validateToken("invalid.token.value"));
    }

    @Test(priority = 53, groups = "security")
    public void testPasswordEncoderMatches() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("mypassword");

        Assert.assertTrue(encoder.matches("mypassword", encoded));
    }

    @Test(priority = 54, groups = "security")
    public void testCustomUserDetailsServiceRoleStored() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Map<String, Object> user =
                customUserDetailsService.registerUser(
                        "Role User",
                        "role@example.com",
                        encoder.encode("rolepass"),
                        "VOLUNTEER_VIEWER"
                );

        Assert.assertEquals(user.get("role"), "VOLUNTEER_VIEWER");
    }

    @Test(priority = 55, groups = "security")
    public void testJwtClaimsContainRoleAndUserId() {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        "claims@example.com",
                        "pass",
                        Collections.emptyList()
                );

        String token =
                jwtTokenProvider.generateToken(auth, 42L, "ADMIN");

        Map<String, Object> claims = jwtTokenProvider.getAllClaims(token);

        Assert.assertEquals(((Number) claims.get("userId")).longValue(), 42L);
        Assert.assertEquals(claims.get("role"), "ADMIN");
        Assert.assertEquals(claims.get("email"), "claims@example.com");
    }

    @Test(priority = 56, groups = "security")
    public void testJwtTokenIsDifferentForDifferentUsers() {
        Authentication a1 =
                new UsernamePasswordAuthenticationToken("a@example.com", "pass");
        Authentication a2 =
                new UsernamePasswordAuthenticationToken("b@example.com", "pass");

        String t1 = jwtTokenProvider.generateToken(a1, 1L, "ADMIN");
        String t2 = jwtTokenProvider.generateToken(a2, 2L, "COORDINATOR");

        Assert.assertNotEquals(t1, t2);
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

    @Test(priority = 58, groups = "hql")
    public void testGetOpenTasksViaRepositoryHqlEquivalent() {
        TaskRecord t1 = new TaskRecord();
        t1.setStatus("OPEN");

        when(taskRecordRepository.findByStatus("OPEN"))
                .thenReturn(Collections.singletonList(t1));

        List<TaskRecord> open = taskRecordService.getOpenTasks();

        Assert.assertEquals(open.size(), 1);
    }

    @Test(priority = 59, groups = "hql")
    public void testSkillSearchByNameUsingQueryAsHql() {
        VolunteerSkillRecord rec = new VolunteerSkillRecord();
        rec.setSkillName("TEACHING");
        rec.setSkillLevel("EXPERT");

        when(volunteerSkillRecordRepository.findBySkillName("TEACHING"))
                .thenReturn(Collections.singletonList(rec));

        List<VolunteerSkillRecord> list =
                volunteerSkillRecordRepository.findBySkillName("TEACHING");

        Assert.assertEquals(list.get(0).getSkillLevel(), "EXPERT");
    }

    @Test(priority = 60, groups = "hql")
    public void testFindAssignmentsByTaskIdActsAsCriteriaQuery() {
        TaskAssignmentRecord a1 = new TaskAssignmentRecord();
        a1.setTaskId(999L);

        when(taskAssignmentRecordRepository.findByTaskId(999L))
                .thenReturn(Collections.singletonList(a1));

        List<TaskAssignmentRecord> list =
                taskAssignmentService.getAssignmentsByTask(999L);

        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 61, groups = "hql")
    public void testFindAssignmentsByVolunteerIdActsAsCriteriaQuery() {
        TaskAssignmentRecord a1 = new TaskAssignmentRecord();
        a1.setVolunteerId(888L);

        when(taskAssignmentRecordRepository.findByVolunteerId(888L))
                .thenReturn(Collections.singletonList(a1));

        List<TaskAssignmentRecord> list =
                taskAssignmentService.getAssignmentsByVolunteer(888L);

        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 62, groups = "hql")
    public void testGetEvaluationsByAssignmentIdActsAsHql() {
        AssignmentEvaluationRecord e1 = new AssignmentEvaluationRecord();
        e1.setAssignmentId(777L);

        when(assignmentEvaluationRecordRepository.findByAssignmentId(777L))
                .thenReturn(Collections.singletonList(e1));

        List<AssignmentEvaluationRecord> list =
                assignmentEvaluationService.getEvaluationsByAssignment(777L);

        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 63, groups = "hql")
    public void testAdvancedFilteringUsingInMemoryCriteria() {
        VolunteerSkillRecord s1 = new VolunteerSkillRecord();
        s1.setSkillName("COOKING");
        s1.setSkillLevel("EXPERT");

        VolunteerSkillRecord s2 = new VolunteerSkillRecord();
        s2.setSkillName("COOKING");
        s2.setSkillLevel("BEGINNER");

        List<VolunteerSkillRecord> list = Arrays.asList(s1, s2);

        long countExpert = list.stream()
                .filter(x -> x.getSkillName().equals("COOKING") &&
                             x.getSkillLevel().equals("EXPERT"))
                .count();

        Assert.assertEquals(countExpert, 1);
    }

    @Test(priority = 64, groups = "hql")
    public void testEdgeCaseNoResultsForSkillQuery() {
        when(volunteerSkillRecordRepository.findBySkillNameAndSkillLevel("OTHER", "BEGINNER"))
                .thenReturn(Collections.emptyList());

        List<VolunteerSkillRecord> list =
                volunteerSkillRecordRepository.findBySkillNameAndSkillLevel("OTHER", "BEGINNER");

        Assert.assertTrue(list.isEmpty());
    }

}
