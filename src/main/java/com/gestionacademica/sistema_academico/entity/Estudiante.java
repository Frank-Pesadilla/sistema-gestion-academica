package com.gestionacademica.sistema_academico.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El carnet es obligatorio")
    @Size(max = 20, message = "El carnet no puede exceder 20 caracteres")
    @Column(unique = true)
    private String carnet;
    
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
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;
    
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
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;

    // Agregar estos getters y setters:
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
    
    // Constructor vacío (requerido por JPA)
    public Estudiante() {}
    
    // Constructor con parámetros
    public Estudiante(String carnet, String nombre, String apellido, String email, 
                     String telefono, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCarnet() {
        return carnet;
    }
    
    public void setCarnet(String carnet) {
        this.carnet = carnet;
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
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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
        return "Estudiante{" +
                "id=" + id +
                ", carnet='" + carnet + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}