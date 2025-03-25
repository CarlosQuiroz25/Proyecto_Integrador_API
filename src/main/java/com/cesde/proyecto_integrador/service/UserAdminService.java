package com.cesde.proyecto_integrador.service;

import java.util.List;

import com.cesde.proyecto_integrador.model.User;

public interface UserAdminService {

    // Operaciones de autenticación
    public User register(User user, String confirmPassword);

    public void changePassword(Long userId, String oldPassword, String newPassword);

    public void resetPassword(Long userId, String newPassword);

    // Operaciones CRUD
    public List<User> findAll();

    public User findById(Long id);

    public User update(Long id, User user);

    public void delete(Long userId);

    // Operaciones adicionales     
    public User findByEmail(String email);   

    public List<User> findByRole(User.Role role);
}
