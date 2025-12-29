# Spring Boot Configuration
spring.application.name=SkillBasedVolunteerTaskAssignor

# Server Configuration
server.port=8080

# Database Configuration (H2 for development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.server.port=8081

# Security (for development)
spring.security.user.name=admin
spring.security.user.password=admin

# Logging
logging.level.com.example.demo=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.springframework.security=INFO

# Disable devtools restart temporarily if causing issues
spring.devtools.restart.enabled=false

# Jackson configuration
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=UTC

# OpenAPI/Swagger configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true