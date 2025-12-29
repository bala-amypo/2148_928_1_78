package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
        // Define server with the specified URL
        Server server = new Server();
        server.setUrl("https://9162.408procr.amypo.ai/");
        server.setDescription("API Server");

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
                    """);

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
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", securityScheme));
    }
}