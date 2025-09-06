package com.gestionacademica.sistema_academico.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    @Column(unique = true)
    private String codigo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Max(value = 10, message = "Los créditos no pueden exceder 10")
    private Integer creditos;
    
    @Column(name = "horas_semanales")
    @Min(value = 1, message = "Las horas semanales deben ser al menos 1")
    @Max(value = 20, message = "Las horas semanales no pueden exceder 20")
    private Integer horasSemanales;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "profesor_id", nullable = false)
private Profesor profesor;

@OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Inscripcion> inscripciones;

// Agregar estos getters y setters:
public Profesor getProfesor() {
    return profesor;
}

public void setProfesor(Profesor profesor) {
    this.profesor = profesor;
}

public List<Inscripcion> getInscripciones() {
    return inscripciones;
}

public void setInscripciones(List<Inscripcion> inscripciones) {
    this.inscripciones = inscripciones;
}
    
    // Constructor vacío (requerido por JPA)
    public Curso() {}
    
    // Constructor con parámetros
    public Curso(String codigo, String nombre, String descripcion, Integer creditos, Integer horasSemanales) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Integer getCreditos() {
        return creditos;
    }
    
    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }
    
    public Integer getHorasSemanales() {
        return horasSemanales;
    }
    
    public void setHorasSemanales(Integer horasSemanales) {
        this.horasSemanales = horasSemanales;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", creditos=" + creditos +
                ", horasSemanales=" + horasSemanales +
                '}';
    }
}