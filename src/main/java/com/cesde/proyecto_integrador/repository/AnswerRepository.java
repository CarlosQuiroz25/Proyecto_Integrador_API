package com.cesde.proyecto_integrador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cesde.proyecto_integrador.model.Answer;
import com.cesde.proyecto_integrador.model.Survey;
import com.cesde.proyecto_integrador.model.User;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByUser(User user);
    
    @Query("SELECT a FROM Answer a WHERE a.question.survey = :survey")
    List<Answer> findBySurvey(Survey survey);
    
    @Query("SELECT a FROM Answer a WHERE a.user = :user AND a.question.survey = :survey")
    List<Answer> findByUserAndSurvey(User user, Survey survey);
    
    @Query("SELECT COUNT(DISTINCT a.user) FROM Answer a WHERE a.question.survey = :survey")
    Long countDistinctUsersBySurvey(Survey survey);
} 