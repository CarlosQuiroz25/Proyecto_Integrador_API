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
import com.cesde.proyecto_integrador.exception.ResourceNotFoundException;

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
            
            // Asegurar que el usuario creador se establezca correctamente
            survey.setCreatedBy(user);
            
            // Validar fechas de la encuesta
            if (survey.getFechaInicio() != null && survey.getFechaFin() != null) {
                if (survey.getFechaInicio().isAfter(survey.getFechaFin())) {
                    log.error("La fecha de inicio no puede ser posterior a la fecha de fin");
                    return ResponseEntity.badRequest().build();
                }
            }
            
            Survey savedSurvey = surveyService.save(survey);
            log.debug("Encuesta creada exitosamente con ID: {}", savedSurvey.getId());
            return ResponseEntity.ok(savedSurvey);
        } catch (Exception e) {
            log.error("Error al crear la encuesta: {}", e.getMessage(), e);
            // No propagar la excepción, devolver un error controlado
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/{surveyId}/questions")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Question> addQuestion(
            @PathVariable Long surveyId,
            @Valid @RequestBody Question question) {
        try {
            log.debug("Agregando pregunta a la encuesta {}: {}", surveyId, question);
            
            // Asegurar que la pregunta no tenga una encuesta ya asignada
            question.setSurvey(null);
            
            Question savedQuestion = surveyService.addQuestion(surveyId, question);
            log.debug("Pregunta agregada exitosamente con ID: {}", savedQuestion.getId());
            return ResponseEntity.ok(savedQuestion);
        } catch (Exception e) {
            log.error("Error al agregar pregunta a la encuesta {}: {}", surveyId, e.getMessage(), e);
            // No propagar la excepción, devolver un error controlado
            return ResponseEntity.status(500).build();
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
            // No propagar la excepción, devolver un error controlado
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        try {
            Survey survey = surveyService.findById(id);
            log.debug("Recuperada encuesta con ID: {}", id);
            return ResponseEntity.ok(survey);
        } catch (ResourceNotFoundException e) {
            log.error("Encuesta no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener encuesta {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Survey> updateSurvey(
            @PathVariable Long id,
            @Valid @RequestBody Survey survey) {
        try {
            log.debug("Actualizando encuesta {}: {}", id, survey);
            
            // Validar fechas de la encuesta
            if (survey.getFechaInicio() != null && survey.getFechaFin() != null) {
                if (survey.getFechaInicio().isAfter(survey.getFechaFin())) {
                    log.error("La fecha de inicio no puede ser posterior a la fecha de fin");
                    return ResponseEntity.badRequest().build();
                }
            }
            
            Survey updatedSurvey = surveyService.update(id, survey);
            log.debug("Encuesta actualizada exitosamente");
            return ResponseEntity.ok(updatedSurvey);
        } catch (ResourceNotFoundException e) {
            log.error("Encuesta no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar encuesta {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        try {
            log.debug("Eliminando encuesta: {}", id);
            surveyService.delete(id);
            log.debug("Encuesta eliminada exitosamente");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar encuesta {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}/questions")
    public ResponseEntity<List<Question>> getSurveyQuestions(@PathVariable Long id) {
        try {
            List<Question> questions = surveyService.findQuestionsBySurvey(id);
            log.debug("Recuperadas {} preguntas para la encuesta {}", questions.size(), id);
            return ResponseEntity.ok(questions);
        } catch (ResourceNotFoundException e) {
            log.error("Encuesta no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener preguntas de la encuesta {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
} 