package com.cesde.proyecto_integrador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerRequestDTO {
    @NotNull(message = "El ID de la pregunta es requerido")
    private Long questionId;
    
    @NotBlank(message = "La respuesta no puede estar vacía")
    private String respuesta;
} 