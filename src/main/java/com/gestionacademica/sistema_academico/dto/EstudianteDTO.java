package com.gestionacademica.sistema_academico.dto;

import java.time.LocalDate;

public class EstudianteDTO {
    
    private Long id;
    private String carnet;
    private String nombreCompleto;     // Combinamos nombre y apellido
    private String email;
    private String telefono;
    private Integer edad;              // Calculada en base a fecha de nacimiento
    private LocalDate fechaIngreso;
    private String semestreActual;     // Calculado en base a fecha de ingreso
    private String estadoAcademico;    // Activo, Inactivo, etc.
    
    // Constructor vacío
    public EstudianteDTO() {}
    
    // Constructor con parámetros
    public EstudianteDTO(Long id, String carnet, String nombreCompleto, String email, 
                        String telefono, Integer edad, LocalDate fechaIngreso, 
                        String semestreActual, String estadoAcademico) {
        this.id = id;
        this.carnet = carnet;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.edad = edad;
        this.fechaIngreso = fechaIngreso;
        this.semestreActual = semestreActual;
        this.estadoAcademico = estadoAcademico;
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
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    public String getSemestreActual() {
        return semestreActual;
    }
    
    public void setSemestreActual(String semestreActual) {
        this.semestreActual = semestreActual;
    }
    
    public String getEstadoAcademico() {
        return estadoAcademico;
    }
    
    public void setEstadoAcademico(String estadoAcademico) {
        this.estadoAcademico = estadoAcademico;
    }
    
    @Override
    public String toString() {
        return "EstudianteDTO{" +
                "id=" + id +
                ", carnet='" + carnet + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", edad=" + edad +
                ", fechaIngreso=" + fechaIngreso +
                ", semestreActual='" + semestreActual + '\'' +
                ", estadoAcademico='" + estadoAcademico + '\'' +
                '}';
    }
}