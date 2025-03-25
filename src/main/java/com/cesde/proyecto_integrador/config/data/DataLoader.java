package com.cesde.proyecto_integrador.config.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cesde.proyecto_integrador.model.Profile;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el admin ya existe
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            // Crear usuario admin
            User adminUser = new User();           
            adminUser.setEmail("admin@admin.com");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setRole(User.Role.ADMIN);
            
            // Crear perfil para el admin
            Profile adminProfile = new Profile();
            adminProfile.setUser(adminUser);
            adminUser.setProfile(adminProfile);
            
            // Guardar usuario (cascadeará el perfil)
            userRepository.save(adminUser);
            
            System.out.println("Usuario administrador creado exitosamente");
        } else {
            System.out.println("Usuario administrador ya existe");
        }       
    }
}
