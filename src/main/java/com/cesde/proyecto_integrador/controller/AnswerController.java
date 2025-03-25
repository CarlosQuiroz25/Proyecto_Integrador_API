package com.cesde.proyecto_integrador.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cesde.proyecto_integrador.dto.SurveyAnswersRequestDTO;
import com.cesde.proyecto_integrador.model.Answer;
import com.cesde.proyecto_integrador.service.AnswerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<Answer>> submitAnswers(
            @Valid @RequestBody SurveyAnswersRequestDTO answersDTO) {
        try {
            log.debug("Recibiendo solicitud para guardar respuestas: {}", answersDTO);
            List<Answer> savedAnswers = answerService.saveAnswers(answersDTO);
            log.debug("Respuestas guardadas exitosamente");
            return ResponseEntity.ok(savedAnswers);
        } catch (Exception e) {
            log.error("Error al guardar las respuestas: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/survey/{surveyId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<List<Answer>> getUserAnswers(
            @PathVariable Long surveyId) {
        return ResponseEntity.ok(answerService.getUserAnswers(surveyId));
    }

    @GetMapping("/survey/{surveyId}/respondents")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Long> getRespondentCount(
            @PathVariable Long surveyId) {
        return ResponseEntity.ok(answerService.getRespondentCount(surveyId));
    }
} 