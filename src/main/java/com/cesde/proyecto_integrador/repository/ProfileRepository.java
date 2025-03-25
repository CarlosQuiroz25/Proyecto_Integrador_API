package com.cesde.proyecto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.proyecto_integrador.model.User;

public interface ProfileRepository extends JpaRepository<User, Long> {

}
