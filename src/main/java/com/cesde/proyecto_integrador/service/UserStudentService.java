package com.cesde.proyecto_integrador.service;

import com.cesde.proyecto_integrador.dto.ProfileDTO;

public interface UserStudentService {
    // Profile
    public ProfileDTO getProfile(Long userId);
    public void updateProfile(Long userId, ProfileDTO profile);

}
