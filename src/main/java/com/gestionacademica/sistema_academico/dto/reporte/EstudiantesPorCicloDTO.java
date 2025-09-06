package com.gestionacademica.sistema_academico.dto.reporte;

public class EstudiantesPorCicloDTO {
    private String cicloAcademico;
    private Long cantidadEstudiantes;
    
    // Constructor para JPQL
    public EstudiantesPorCicloDTO(String cicloAcademico, Long cantidadEstudiantes) {
        this.cicloAcademico = cicloAcademico;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }
    
    // Getters y Setters
    public String getCicloAcademico() {
        return cicloAcademico;
    }
    
    public void setCicloAcademico(String cicloAcademico) {
        this.cicloAcademico = cicloAcademico;
    }
    
    public Long getCantidadEstudiantes() {
        return cantidadEstudiantes;
    }
    
    public void setCantidadEstudiantes(Long cantidadEstudiantes) {
        this.cantidadEstudiantes = cantidadEstudiantes;
    }
}