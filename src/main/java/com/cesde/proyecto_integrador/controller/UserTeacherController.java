package com.cesde.proyecto_integrador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cesde.proyecto_integrador.dto.ProfileDTO;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.service.UserStudentService;

public class UserTeacherController {
    @Autowired
    private UserStudentService userStudentService;

    // Profile
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO>  getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userStudentService.getProfile(user.getId())) ;
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@AuthenticationPrincipal User user, @RequestBody ProfileDTO profile) {
        userStudentService.updateProfile(user.getId(), profile);
        return ResponseEntity.ok("Profile updated successfully");
    }
}
