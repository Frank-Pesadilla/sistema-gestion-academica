package com.gestionacademica.sistema_academico.dto;

public class CursoDTO {
    
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer creditos;
    private Integer horasSemanales;
    private String nivelDificultad;    // Calculado: Básico, Intermedio, Avanzado
    private String cargaAcademica;     // Calculado: Baja, Media, Alta
    
    // Constructor vacío
    public CursoDTO() {}
    
    // Constructor con parámetros
    public CursoDTO(Long id, String codigo, String nombre, String descripcion, 
                   Integer creditos, Integer horasSemanales, String nivelDificultad, 
                   String cargaAcademica) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.horasSemanales = horasSemanales;
        this.nivelDificultad = nivelDificultad;
        this.cargaAcademica = cargaAcademica;
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
    
    public String getNivelDificultad() {
        return nivelDificultad;
    }
    
    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }
    
    public String getCargaAcademica() {
        return cargaAcademica;
    }
    
    public void setCargaAcademica(String cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
    }
    
    @Override
    public String toString() {
        return "CursoDTO{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", creditos=" + creditos +
                ", horasSemanales=" + horasSemanales +
                ", nivelDificultad='" + nivelDificultad + '\'' +
                ", cargaAcademica='" + cargaAcademica + '\'' +
                '}';
    }
}