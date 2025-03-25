package com.cesde.proyecto_integrador.dto;

import com.cesde.proyecto_integrador.model.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    @Email(message = "Email must be in a valid format")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    private String password_confirmation;
    private User.Role role;
}
