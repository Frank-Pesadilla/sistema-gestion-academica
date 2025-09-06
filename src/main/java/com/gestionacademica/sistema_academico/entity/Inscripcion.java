package com.gestionacademica.sistema_academico.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;
    
    @Column(name = "ciclo_academico")
    private String cicloAcademico;
    
    @Column(name = "nota_final")
    private Double notaFinal;
    
    // Constructor por defecto
    public Inscripcion() {}
    
    // Constructor con parámetros
    public Inscripcion(Estudiante estudiante, Curso curso, LocalDate fechaInscripcion, String cicloAcademico) {
        this.estudiante = estudiante;
        this.curso = curso;
        this.fechaInscripcion = fechaInscripcion;
        this.cicloAcademico = cicloAcademico;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Curso getCurso() {
        return curso;
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }
    
    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
    
    public String getCicloAcademico() {
        return cicloAcademico;
    }
    
    public void setCicloAcademico(String cicloAcademico) {
        this.cicloAcademico = cicloAcademico;
    }
    
    public Double getNotaFinal() {
        return notaFinal;
    }
    
    public void setNotaFinal(Double notaFinal) {
        this.notaFinal = notaFinal;
    }
}