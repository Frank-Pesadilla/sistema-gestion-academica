package com.gestionacademica.sistema_academico.dto;

import java.time.LocalDate;

public class ProfesorDTO {
    
    private Long id;
    private String nombreCompleto;  // Combinamos nombre y apellido
    private String email;
    private String telefono;
    private String especialidad;
    private LocalDate fechaContratacion;
    private Integer añosExperiencia;  // Calculado en base a fecha de contratación
    
    // Constructor vacío
    public ProfesorDTO() {}
    
    // Constructor con parámetros
    public ProfesorDTO(Long id, String nombreCompleto, String email, String telefono, 
                      String especialidad, LocalDate fechaContratacion, Integer añosExperiencia) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.fechaContratacion = fechaContratacion;
        this.añosExperiencia = añosExperiencia;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Integer getAñosExperiencia() {
        return añosExperiencia;
    }
    
    public void setAñosExperiencia(Integer añosExperiencia) {
        this.añosExperiencia = añosExperiencia;
    }
    
    @Override
    public String toString() {
        return "ProfesorDTO{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                ", añosExperiencia=" + añosExperiencia +
                '}';
    }
}