package com.cesde.proyecto_integrador.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cesde.proyecto_integrador.dto.UserLoginDTO;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.security.JwtUtil;
import com.cesde.proyecto_integrador.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO loginUser) {
        User user = new User();
        user.setEmail(loginUser.getEmail());
        user.setPassword(loginUser.getPassword());

        User authenticatedUser = authService.login(user);

        String token = jwtUtil.generateToken(authenticatedUser.getEmail(), authenticatedUser.getRole().toString());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", authenticatedUser.getEmail());
        response.put("role", authenticatedUser.getRole().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }
}