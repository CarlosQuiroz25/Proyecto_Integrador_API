package com.cesde.proyecto_integrador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesde.proyecto_integrador.model.Survey;
import com.cesde.proyecto_integrador.model.User;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByActivaTrue();
    List<Survey> findByActivaTrueOrderByFechaInicioDesc();
    List<Survey> findByCreatedBy(User user);
} 