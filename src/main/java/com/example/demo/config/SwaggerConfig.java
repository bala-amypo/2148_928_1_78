project-root
│
├── pom.xml
│
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── demo
│       │
│       │               ├── DemoApplication.java
│       │
│       │               ├── config
│       │               │   ├── SecurityConfig.java
│       │               │   └── SwaggerConfig.java
│       │
│       │               ├── controller
│       │               │   ├── AuthController.java
│       │               │   ├── VolunteerProfileController.java
│       │               │   ├── VolunteerSkillController.java
│       │               │   ├── TaskRecordController.java
│       │               │   ├── TaskAssignmentController.java
│       │               │   └── AssignmentEvaluationController.java
│       │
│       │               ├── dto
│       │               │   ├── RegisterRequest.java
│       │               │   ├── AuthRequest.java
│       │               │   ├── AuthResponse.java
│       │               │   ├── AvailabilityUpdateRequest.java
│       │               │   ├── AssignmentStatusUpdateRequest.java
│       │               │   └── EvaluationRequest.java
│       │
│       │               ├── exception
│       │               │   ├── BadRequestException.java
│       │               │   ├── ResourceNotFoundException.java
│       │               │   └── GlobalExceptionHandler.java
│       │
│       │               ├── model
│       │               │   ├── User.java
│       │               │   ├── VolunteerProfile.java
│       │               │   ├── VolunteerSkillRecord.java
│       │               │   ├── TaskRecord.java
│       │               │   ├── TaskAssignmentRecord.java
│       │               │   └── AssignmentEvaluationRecord.java
│       │
│       │               ├── repository
│       │               │   ├── UserRepository.java
│       │               │   ├── VolunteerProfileRepository.java
│       │               │   ├── VolunteerSkillRecordRepository.java
│       │               │   ├── TaskRecordRepository.java
│       │               │   ├── TaskAssignmentRecordRepository.java
│       │               │   └── AssignmentEvaluationRecordRepository.java
│       │
│       │               ├── security
│       │               │   ├── JwtTokenProvider.java
│       │               │   ├── JwtAuthenticationFilter.java
│       │               │   └── CustomUserDetailsService.java
│       │
│       │               ├── service
│       │               │   ├── UserService.java
│       │               │   ├── VolunteerProfileService.java
│       │               │   ├── VolunteerSkillService.java
│       │               │   ├── TaskRecordService.java
│       │               │   ├── TaskAssignmentService.java
│       │               │   └── AssignmentEvaluationService.java
│       │
│       │               ├── servlet
│       │               │   └── HelloServlet.java
│       │
│       │               └── util
│       │                   └── SkillLevelUtil.java
│       │
│       └── resources
│           └── application.properties
