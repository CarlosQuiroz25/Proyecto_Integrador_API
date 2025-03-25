package com.cesde.proyecto_integrador.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesde.proyecto_integrador.exception.ResourceNotFoundException;
import com.cesde.proyecto_integrador.model.Question;
import com.cesde.proyecto_integrador.model.Survey;
import com.cesde.proyecto_integrador.repository.QuestionRepository;
import com.cesde.proyecto_integrador.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    @Transactional
    public Question addQuestion(Long surveyId, Question question) {
        Survey survey = findById(surveyId);
        question.setSurvey(survey);
        return questionRepository.save(question);
    }

    public List<Survey> findActiveSurveys() {
        return surveyRepository.findByActivaTrue();
    }

    public Survey findById(Long id) {
        return surveyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Encuesta no encontrada con ID: " + id));
    }

    @Transactional
    public Survey update(Long id, Survey surveyDetails) {
        Survey survey = findById(id);
        survey.setTitulo(surveyDetails.getTitulo());
        survey.setDescripcion(surveyDetails.getDescripcion());
        survey.setFechaInicio(surveyDetails.getFechaInicio());
        survey.setFechaFin(surveyDetails.getFechaFin());
        survey.setActiva(surveyDetails.isActiva());
        return surveyRepository.save(survey);
    }

    @Transactional
    public void delete(Long id) {
        surveyRepository.deleteById(id);
    }

    public List<Question> findQuestionsBySurvey(Long surveyId) {
        Survey survey = findById(surveyId);
        return questionRepository.findBySurvey(survey);
    }
} 