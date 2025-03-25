package com.cesde.proyecto_integrador.dto;

import com.cesde.proyecto_integrador.model.User;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @Email(message = "Email must be in a valid format")
    private String email;
    private User.Role role;
}