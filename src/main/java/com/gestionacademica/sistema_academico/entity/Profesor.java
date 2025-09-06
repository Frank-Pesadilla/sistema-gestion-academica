package com.gestionacademica.sistema_academico.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profesores")
public class Profesor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(unique = true)
    private String email;
    
    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    private String telefono;
    
    @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
    private String especialidad;
    
    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;
    
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
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Curso> cursos;

    // Agregar estos getters y setters:
    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
    
    // Constructor vacío (requerido por JPA)
    public Profesor() {}
    
    // Constructor con parámetros
    public Profesor(String nombre, String apellido, String email, String telefono, 
                   String especialidad, LocalDate fechaContratacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.fechaContratacion = fechaContratacion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }
    
    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
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
        return "Profesor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                '}';
    }
}