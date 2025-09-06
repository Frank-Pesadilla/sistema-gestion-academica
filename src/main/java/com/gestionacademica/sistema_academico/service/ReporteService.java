package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.dto.reporte.CursosPorProfesorDTO;
import com.gestionacademica.sistema_academico.dto.reporte.EstudiantesPorCicloDTO;
import com.gestionacademica.sistema_academico.dto.reporte.NotaPromedioPorCursoDTO;
import com.gestionacademica.sistema_academico.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    /**
     * Obtiene el número total de cursos que imparte cada profesor
     * @return Lista de DTOs con nombre del profesor y cantidad de cursos
     */
    public List<CursosPorProfesorDTO> obtenerCursosPorProfesor() {
        return inscripcionRepository.findCursosPorProfesor();
        //return inscripcionRepository.findCursosPorProfesor();
    }
    
    /**
     * Calcula la nota promedio para cada curso
     * @return Lista de DTOs con nombre del curso y nota promedio
     */
    public List<NotaPromedioPorCursoDTO> obtenerNotaPromedioPorCurso() {
        return inscripcionRepository.findNotaPromedioPorCurso();
    }
    
    /**
     * Cuenta cuántos estudiantes están inscritos por cada ciclo académico
     * @return Lista de DTOs con ciclo académico y cantidad de estudiantes
     */
    public List<EstudiantesPorCicloDTO> obtenerEstudiantesPorCiclo() {
        return inscripcionRepository.findEstudiantesPorCiclo();
    }
    
    /**
     * Obtiene los 3 cursos con la nota promedio más alta
     * @return Lista de máximo 3 DTOs con los cursos mejor calificados
     */
    public List<NotaPromedioPorCursoDTO> obtenerTop3CursosConMejorPromedio() {
        List<NotaPromedioPorCursoDTO> todosCursos = inscripcionRepository.findTop3CursosConMejorPromedio();
        
        // Limitar a los primeros 3 resultados
        if (todosCursos.size() > 3) {
            return todosCursos.subList(0, 3);
        }
        
        return todosCursos;
    }
}