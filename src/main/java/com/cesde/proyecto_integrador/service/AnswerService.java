package com.cesde.proyecto_integrador.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.proyecto_integrador.dto.AnswerRequestDTO;
import com.cesde.proyecto_integrador.dto.SurveyAnswersRequestDTO;
import com.cesde.proyecto_integrador.exception.ResourceNotFoundException;
import com.cesde.proyecto_integrador.model.Answer;
import com.cesde.proyecto_integrador.model.Question;
import com.cesde.proyecto_integrador.model.Survey;
import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.repository.AnswerRepository;
import com.cesde.proyecto_integrador.repository.QuestionRepository;
import com.cesde.proyecto_integrador.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final UserService userService;

    @Transactional
    public List<Answer> saveAnswers(SurveyAnswersRequestDTO answersDTO) {
        try {
            // Validar que el surveyId no sea nulo
            if (answersDTO.getSurveyId() == null) {
                throw new IllegalArgumentException("El ID de la encuesta no puede ser nulo");
            }

            // Obtener el usuario actual
            User currentUser = userService.getCurrentUser();
            log.debug("Usuario actual: {}", currentUser.getEmail());

            // Obtener la encuesta
            Survey survey = surveyRepository.findById(answersDTO.getSurveyId())
                .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada con ID: " + answersDTO.getSurveyId()));

            // Validar que la encuesta esté activa
            if (!survey.isActiva()) {
                throw new IllegalStateException("La encuesta no está activa");
            }

            // Validar que la lista de respuestas no sea nula o vacía
            if (answersDTO.getRespuestas() == null || answersDTO.getRespuestas().isEmpty()) {
                throw new IllegalArgumentException("Debe proporcionar al menos una respuesta");
            }

            // Procesar cada respuesta
            return answersDTO.getRespuestas().stream()
                .map(answerDTO -> createAnswer(answerDTO, currentUser, survey))
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error al guardar las respuestas: {}", e.getMessage());
            throw e;
        }
    }

    private Answer createAnswer(AnswerRequestDTO answerDTO, User user, Survey survey) {
        // Validar que el questionId no sea nulo
        if (answerDTO.getQuestionId() == null) {
            throw new IllegalArgumentException("El ID de la pregunta no puede ser nulo");
        }

        // Obtener la pregunta y validar que pertenezca a la encuesta
        Question question = questionRepository.findById(answerDTO.getQuestionId())
            .orElseThrow(() -> new ResourceNotFoundException("Pregunta no encontrada con ID: " + answerDTO.getQuestionId()));

        if (!question.getSurvey().getId().equals(survey.getId())) {
            throw new IllegalArgumentException("La pregunta no pertenece a la encuesta especificada");
        }

        // Validar que la respuesta no sea nula o vacía
        if (answerDTO.getRespuesta() == null || answerDTO.getRespuesta().trim().isEmpty()) {
            throw new IllegalArgumentException("La respuesta no puede estar vacía");
        }

        // Crear y guardar la respuesta
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setUser(user);
        answer.setRespuesta(answerDTO.getRespuesta().trim());
        
        return answerRepository.save(answer);
    }

    public List<Answer> getUserAnswers(Long surveyId) {
        User currentUser = userService.getCurrentUser();
        Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada con ID: " + surveyId));
            
        return answerRepository.findByUserAndSurvey(currentUser, survey);
    }

    public Long getRespondentCount(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada con ID: " + surveyId));
            
        return answerRepository.countDistinctUsersBySurvey(survey);
    }
} 