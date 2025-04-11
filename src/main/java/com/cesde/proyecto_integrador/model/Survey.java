package com.cesde.proyecto_integrador.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "surveys")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @NotBlank(message = "El título no puede estar vacío")
    @Column(nullable = false)
    private String titulo;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;
    
    @NotNull(message = "La fecha de inicio no puede estar vacía")
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @NotNull(message = "La fecha de fin no puede estar vacía")
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(nullable = false)
    private boolean activa = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
    
    @JsonManagedReference(value = "survey-questions")
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Question> preguntas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;
} 