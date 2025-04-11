package com.cesde.proyecto_integrador.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "La pregunta no puede estar vacía")
    @Column(nullable = false)
    private String pregunta;
    
    @NotNull(message = "El tipo de pregunta no puede estar vacío")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pregunta", nullable = false)
    private QuestionType tipoPregunta;
    
    @Column(name = "opciones_respuesta", columnDefinition = "TEXT")
    private String opcionesRespuesta;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
    
    @JsonBackReference(value = "survey-questions")
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    
    @JsonManagedReference(value = "question-answers")
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Answer> respuestas;
} 