package com.cesde.proyecto_integrador.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cesde.proyecto_integrador.dto.ProfileDTO;
import com.cesde.proyecto_integrador.exception.ResourceNotFoundException;
import com.cesde.proyecto_integrador.model.Profile;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.repository.ProfileRepository;
import com.cesde.proyecto_integrador.repository.UserRepository;
import com.cesde.proyecto_integrador.service.UserStudentService;

@Service
public class UserStudentServiceImpl implements UserStudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    // Profile
    @Override
    public ProfileDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Profile profile = user.getProfile();
        if (profile == null) {
            throw new ResourceNotFoundException("Profile not found for user with id: " + userId);
        }

        return new ProfileDTO(
                profile.getName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getAddress(),
                profile.getUrlPhoto());

    }

    @Override
    public void updateProfile(Long userId, ProfileDTO profile) {
        User user = userRepository.findById(userId).orElse(null);
        Profile updateProfile = user.getProfile();
        updateProfile.setName(profile.getName());
        updateProfile.setLastName(profile.getLastName());
        updateProfile.setPhone(profile.getPhone());
        updateProfile.setAddress(profile.getAddress());
        updateProfile.setUrlPhoto(profile.getUrlPhoto());
        profileRepository.save(user);
    }

}
