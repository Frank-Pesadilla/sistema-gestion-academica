package com.gestionacademica.sistema_academico.dto.reporte;

public class CursosPorProfesorDTO {
    private String nombreProfesor;
    private Long cantidadCursos;
    
    // Constructor para JPQL
    public CursosPorProfesorDTO(String nombreProfesor, Long cantidadCursos) {
        this.nombreProfesor = nombreProfesor;
        this.cantidadCursos = cantidadCursos;
    }
    
    // Getters y Setters
    public String getNombreProfesor() {
        return nombreProfesor;
    }
    
    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
    
    public Long getCantidadCursos() {
        return cantidadCursos;
    }
    
    public void setCantidadCursos(Long cantidadCursos) {
        this.cantidadCursos = cantidadCursos;
    }
}
