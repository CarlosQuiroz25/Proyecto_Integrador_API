package com.cesde.proyecto_integrador.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.cesde.proyecto_integrador.model.Survey;
import com.cesde.proyecto_integrador.model.Question;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.service.SurveyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Survey> createSurvey(
            @Valid @RequestBody Survey survey,
            @AuthenticationPrincipal User user) {
        try {
            log.debug("Usuario autenticado: {}", user.getEmail());
            log.debug("Rol del usuario: {}", user.getRole());
            log.debug("Creando nueva encuesta: {}", survey);
            
            survey.setCreatedBy(user);
            Survey savedSurvey = surveyService.save(survey);
            log.debug("Encuesta creada exitosamente con ID: {}", savedSurvey.getId());
            return ResponseEntity.ok(savedSurvey);
        } catch (Exception e) {
            log.error("Error al crear la encuesta: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/{surveyId}/questions")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Question> addQuestion(
            @PathVariable Long surveyId,
            @Valid @RequestBody Question question) {
        try {
            log.debug("Agregando pregunta a la encuesta {}: {}", surveyId, question);
            Question savedQuestion = surveyService.addQuestion(surveyId, question);
            log.debug("Pregunta agregada exitosamente con ID: {}", savedQuestion.getId());
            return ResponseEntity.ok(savedQuestion);
        } catch (Exception e) {
            log.error("Error al agregar pregunta a la encuesta {}: {}", surveyId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Survey>> getActiveSurveys() {
        try {
            List<Survey> surveys = surveyService.findActiveSurveys();
            log.debug("Recuperadas {} encuestas activas", surveys.size());
            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            log.error("Error al obtener encuestas activas: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        try {
            Survey survey = surveyService.findById(id);
            log.debug("Recuperada encuesta con ID: {}", id);
            return ResponseEntity.ok(survey);
        } catch (Exception e) {
            log.error("Error al obtener encuesta {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Survey> updateSurvey(
            @PathVariable Long id,
            @Valid @RequestBody Survey survey) {
        try {
            log.debug("Actualizando encuesta {}: {}", id, survey);
            Survey updatedSurvey = surveyService.update(id, survey);
            log.debug("Encuesta actualizada exitosamente");
            return ResponseEntity.ok(updatedSurvey);
        } catch (Exception e) {
            log.error("Error al actualizar encuesta {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        try {
            log.debug("Eliminando encuesta: {}", id);
            surveyService.delete(id);
            log.debug("Encuesta eliminada exitosamente");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar encuesta {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<Question>> getSurveyQuestions(@PathVariable Long id) {
        try {
            List<Question> questions = surveyService.findQuestionsBySurvey(id);
            log.debug("Recuperadas {} preguntas para la encuesta {}", questions.size(), id);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            log.error("Error al obtener preguntas de la encuesta {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
} 