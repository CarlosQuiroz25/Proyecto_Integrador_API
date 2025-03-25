package com.cesde.proyecto_integrador.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Home", description = "API for general system information")
public class HomeController {

    @Operation(
        summary = "Get general information",
        description = "Returns general information about the API including title, version, description, documentation URL, and author information"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved system information with all fields populated"
    )
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> welcome(
            @Parameter(description = "HTTP request object used to obtain the server URL for documentation links") HttpServletRequest request) {
        String serverUrl = request.getRequestURL().toString();

        Map<String, String> response = new HashMap<>();
        response.put("title", "CESDE API");
        response.put("version", "1.0");
        response.put("description", "API Backend II");
        response.put("Swagger UI", serverUrl + "swagger-ui/index.html");
        response.put("JSON de OpenAPI", serverUrl + "v3/api-docs");
        response.put("YAML de OpenAPI", serverUrl + "api-docs.yaml");
        response.put("author", "Equipo 7");

        return ResponseEntity.ok(response);
    }
}