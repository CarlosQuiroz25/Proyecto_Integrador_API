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
import com.cesde.proyecto_integrador.repository.UserRepository;

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
    private final UserRepository userRepository;

    @Transactional
    public List<Answer> saveAnswers(SurveyAnswersRequestDTO answersDTO) {
        try {
            log.debug("===== SERVICIO: INICIO GUARDADO DE RESPUESTAS =====");
            
            // Validar que el surveyId no sea nulo
            if (answersDTO.getSurveyId() == null) {
                log.error("El ID de la encuesta es nulo");
                throw new IllegalArgumentException("El ID de la encuesta no puede ser nulo");
            }
            log.debug("SurveyId validado: {}", answersDTO.getSurveyId());

            // Obtener el usuario actual o crear uno anónimo si no hay usuario autenticado
            User currentUser = obtenerUsuarioActualOAnonimo();
            log.debug("Usuario seleccionado: {} (ID: {})", 
                    currentUser.getEmail(), 
                    currentUser.getId());

            // Obtener la encuesta
            log.debug("Buscando encuesta con ID: {}", answersDTO.getSurveyId());
            Survey survey = surveyRepository.findById(answersDTO.getSurveyId())
                .orElseThrow(() -> {
                    log.error("Encuesta no encontrada con ID: {}", answersDTO.getSurveyId());
                    return new ResourceNotFoundException("Encuesta no encontrada con ID: " + answersDTO.getSurveyId());
                });
            log.debug("Encuesta encontrada: {} (ID: {})", survey.getTitulo(), survey.getId());

            // Validar que la encuesta esté activa
            if (!survey.isActiva()) {
                log.error("La encuesta no está activa: {}", survey.getId());
                throw new IllegalStateException("La encuesta no está activa");
            }
            log.debug("Encuesta activa: {}", survey.isActiva());

            // Validar que la lista de respuestas no sea nula o vacía
            if (answersDTO.getRespuestas() == null || answersDTO.getRespuestas().isEmpty()) {
                log.error("Lista de respuestas nula o vacía");
                throw new IllegalArgumentException("Debe proporcionar al menos una respuesta");
            }
            log.debug("Número de respuestas a procesar: {}", answersDTO.getRespuestas().size());

            // Procesar cada respuesta
            log.debug("Procesando respuestas...");
            List<Answer> savedAnswers = answersDTO.getRespuestas().stream()
                .map(answerDTO -> {
                    log.debug("Procesando respuesta para pregunta ID: {}", answerDTO.getQuestionId());
                    Answer answer = createAnswer(answerDTO, currentUser, survey);
                    log.debug("Respuesta procesada y guardada con ID: {}", answer.getId());
                    return answer;
                })
                .collect(Collectors.toList());
            
            log.debug("Total de respuestas guardadas: {}", savedAnswers.size());
            log.debug("===== SERVICIO: FIN GUARDADO DE RESPUESTAS =====");
            
            return savedAnswers;

        } catch (Exception e) {
            log.error("===== SERVICIO: ERROR AL GUARDAR RESPUESTAS =====");
            log.error("Tipo de excepción: {}", e.getClass().getName());
            log.error("Mensaje de error: {}", e.getMessage());
            log.error("Causa raíz: {}", e.getCause() != null ? e.getCause().getMessage() : "No hay causa raíz");
            log.error("Stack trace:", e);
            log.error("===== SERVICIO: FIN ERROR AL GUARDAR RESPUESTAS =====");
            throw e;
        }
    }

    /**
     * Obtiene el usuario actual autenticado o un usuario anónimo si no hay autenticación
     */
    private User obtenerUsuarioActualOAnonimo() {
        try {
            User currentUser = userService.getCurrentUser();
            log.debug("Usuario autenticado obtenido: {} (ID: {}, Rol: {})", 
                    currentUser.getEmail(), 
                    currentUser.getId(), 
                    currentUser.getRole());
            return currentUser;
        } catch (Exception e) {
            log.debug("No hay usuario autenticado, usando usuario anónimo");
            // Buscar un usuario anónimo existente o usar el primer usuario disponible
            return userRepository.findByEmail("anonimo@example.com")
                    .orElseGet(() -> {
                        log.debug("Usando el primer usuario disponible como anónimo");
                        return userRepository.findAll().stream().findFirst()
                                .orElseThrow(() -> new IllegalStateException("No hay usuarios disponibles en el sistema"));
                    });
        }
    }

    private Answer createAnswer(AnswerRequestDTO answerDTO, User user, Survey survey) {
        try {
            log.debug("===== CREANDO RESPUESTA =====");
            
            // Validar que el questionId no sea nulo
            if (answerDTO.getQuestionId() == null) {
                log.error("El ID de la pregunta es nulo");
                throw new IllegalArgumentException("El ID de la pregunta no puede ser nulo");
            }
            log.debug("QuestionId validado: {}", answerDTO.getQuestionId());

            // Obtener la pregunta y validar que pertenezca a la encuesta
            log.debug("Buscando pregunta con ID: {}", answerDTO.getQuestionId());
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                .orElseThrow(() -> {
                    log.error("Pregunta no encontrada con ID: {}", answerDTO.getQuestionId());
                    return new ResourceNotFoundException("Pregunta no encontrada con ID: " + answerDTO.getQuestionId());
                });
            log.debug("Pregunta encontrada: {} (ID: {}, Tipo: {})", 
                    question.getPregunta(), 
                    question.getId(), 
                    question.getTipoPregunta());

            if (!question.getSurvey().getId().equals(survey.getId())) {
                log.error("La pregunta {} no pertenece a la encuesta {} (pertenece a la encuesta {})", 
                        question.getId(), 
                        survey.getId(), 
                        question.getSurvey().getId());
                throw new IllegalArgumentException("La pregunta no pertenece a la encuesta especificada");
            }
            log.debug("Pregunta validada como parte de la encuesta");

            // Validar que la respuesta no sea nula o vacía
            if (answerDTO.getRespuesta() == null || answerDTO.getRespuesta().trim().isEmpty()) {
                log.error("La respuesta es nula o vacía");
                throw new IllegalArgumentException("La respuesta no puede estar vacía");
            }
            log.debug("Respuesta validada: {}", answerDTO.getRespuesta());

            // Crear y guardar la respuesta
            log.debug("Creando objeto Answer...");
            Answer answer = new Answer();
            answer.setQuestion(question);
            answer.setUser(user);
            answer.setRespuesta(answerDTO.getRespuesta().trim());
            
            log.debug("Guardando respuesta en la base de datos...");
            Answer savedAnswer = answerRepository.save(answer);
            log.debug("Respuesta guardada con ID: {}", savedAnswer.getId());
            log.debug("===== FIN CREANDO RESPUESTA =====");
            
            return savedAnswer;
        } catch (Exception e) {
            log.error("===== ERROR AL CREAR RESPUESTA =====");
            log.error("Tipo de excepción: {}", e.getClass().getName());
            log.error("Mensaje de error: {}", e.getMessage());
            log.error("Stack trace:", e);
            log.error("===== FIN ERROR AL CREAR RESPUESTA =====");
            throw e;
        }
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