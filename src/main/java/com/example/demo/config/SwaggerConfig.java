package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define servers
        Server localServer = new Server();
        localServer.setUrl("http://localhost:9001");
        localServer.setDescription("Local Development Server");
        
        Server customServer = new Server();
        customServer.setUrl("https://9162.408procr.amypo.ai/");
        customServer.setDescription("Production Deployment Server");

        // Contact information
        Contact contact = new Contact();
        contact.setName("Skill-Based Volunteer Task Assignor");
        contact.setEmail("support@example.com");
        contact.setUrl("https://9162.408procr.amypo.ai");

        // License information
        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        // API Info
        Info info = new Info()
                .title("Skill-Based Volunteer Task Assignor API")
                .version("1.0.0")
                .description("""
                    ## REST API for Volunteer Task Management System
                    
                    This API manages volunteers, their skills, tasks, assignments, and evaluations.
                    
                    ### Key Features:
                    - **Volunteer Management**: Register, update, and manage volunteer profiles
                    - **Skill Tracking**: Record and match volunteer skills with task requirements
                    - **Task Assignment**: Automatically assign tasks based on skill matching
                    - **Evaluation System**: Rate and provide feedback on completed assignments
                    - **JWT Security**: Secure authentication and authorization
                    
                    ### Authentication:
                    - JWT token based authentication
                    - Roles: ADMIN, COORDINATOR, VOLUNTEER_VIEWER
                    
                    ### Access Points:
                    - **API Documentation**: https://9162.408procr.amypo.ai/swagger-ui.html
                    - **OpenAPI Spec**: https://9162.408procr.amypo.ai/v3/api-docs
                    - **Health Check**: https://9162.408procr.amypo.ai/actuator/health
                    """)
                .termsOfService("https://9162.408procr.amypo.ai/terms")
                .contact(contact)
                .license(license);

        // JWT Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .description("Enter JWT token in format: Bearer {token}")
                .in(SecurityScheme.In.HEADER);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, customServer))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", securityScheme));
    }
}