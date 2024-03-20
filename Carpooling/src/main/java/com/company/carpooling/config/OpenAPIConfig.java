package com.company.carpooling.config;

import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Info information = new Info()
                .title("Carpooling")
                .version("1.0")
                .description("Carpooling enables you to share your travel from one location to another with other passengers.");
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
