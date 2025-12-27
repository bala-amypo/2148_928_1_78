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
        localServer.setUrl("http://localhost:9001/api");
        localServer.setDescription("Local Development Server");
        
        Server customServer = new Server();
        customServer.setUrl("https://9162.408procr.amypo.ai/");
        customServer.setDescription("Custom Deployment Server");

        // Contact information
        Contact contact = new Contact();
        contact.setName("Demo Application");
        contact.setEmail("support@example.com");
        contact.setUrl("https://9162.408procr.amypo.ai/");

        // License information
        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        // API Info
        Info info = new Info()
                .title("Volunteer Task Assignor API")
                .version("1.0.0")
                .description("""
                    ## Skill-Based Volunteer Task Assignment System
                    
                    This API provides endpoints for managing volunteers, tasks, assignments, and evaluations.
                    
                    ### Features:
                    - Volunteer profile management
                    - Skill tracking and matching
                    - Task assignment based on skills
                    - Assignment evaluation and feedback
                    
                    ### Authentication:
                    - JWT token based authentication
                    - Roles: ADMIN, COORDINATOR, VOLUNTEER_VIEWER
                    
                    ### API Documentation:
                    - Swagger UI: https://9162.408procr.amypo.ai/swagger-ui.html
                    - OpenAPI Spec: https://9162.408procr.amypo.ai/v3/api-docs
                    """)
                .termsOfService("https://example.com/terms")
                .contact(contact)
                .license(license);

        // JWT Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .description("Enter JWT token")
                .in(SecurityScheme.In.HEADER);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, customServer))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", securityScheme));
    }
}