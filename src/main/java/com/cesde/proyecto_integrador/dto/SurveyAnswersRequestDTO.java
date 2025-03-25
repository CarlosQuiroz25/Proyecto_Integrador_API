package com.cesde.proyecto_integrador.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SurveyAnswersRequestDTO {
    @NotNull(message = "El ID de la encuesta es requerido")
    private Long surveyId;
    
    @NotEmpty(message = "Debe proporcionar al menos una respuesta")
    @Valid
    private List<AnswerRequestDTO> respuestas;
} 