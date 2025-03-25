package com.cesde.proyecto_integrador.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("API - Proyecto Integrador - Backend II")  // Custom title                        
                .description("CESDE - Desarrollo de Software"))
                .addServersItem(new Server()
                .url("http://localhost:8080")
                .description("Servidor Local"));                
    }
}
