package com.gestionacademica.sistema_academico.dto.reporte;

public class NotaPromedioPorCursoDTO {
    private String nombreCurso;
    private Double notaPromedio;
    
    // Constructor para JPQL
    public NotaPromedioPorCursoDTO(String nombreCurso, Double notaPromedio) {
        this.nombreCurso = nombreCurso;
        this.notaPromedio = notaPromedio;
    }
    
    // Getters y Setters
    public String getNombreCurso() {
        return nombreCurso;
    }
    
    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }
    
    public Double getNotaPromedio() {
        return notaPromedio;
    }
    
    public void setNotaPromedio(Double notaPromedio) {
        this.notaPromedio = notaPromedio;
    }
}