package com.cesde.proyecto_integrador.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cesde.proyecto_integrador.exception.AuthenticationException;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.repository.UserRepository;
import com.cesde.proyecto_integrador.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(User user) {
        User userLogin = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new AuthenticationException("Email not found"));
            
        if (!passwordEncoder.matches(user.getPassword(), userLogin.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }
        return userLogin;
    }

}
