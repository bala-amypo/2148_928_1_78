package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Volunteer Skill Matcher API")
                        .version("1.0")
                        .description("Backend API for Volunteer Skill Matcher"))
                .addServersItem(new Server()
                        .url("https://a7fda69f279b-8405.pro604cr.amypo.ai/proxy/9001/")); // Proxy URL
    }
}
