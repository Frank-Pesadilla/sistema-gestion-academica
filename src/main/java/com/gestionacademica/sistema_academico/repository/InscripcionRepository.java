package com.gestionacademica.sistema_academico.repository;

import com.gestionacademica.sistema_academico.dto.reporte.CursosPorProfesorDTO;
import com.gestionacademica.sistema_academico.dto.reporte.EstudiantesPorCicloDTO;
import com.gestionacademica.sistema_academico.dto.reporte.NotaPromedioPorCursoDTO;
import com.gestionacademica.sistema_academico.entity.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    // Reporte 1: Número total de cursos que imparte cada profesor
    @Query("SELECT NEW com.gestionacademica.sistema_academico.dto.reportes.CursosPorProfesorDTO(" +
           "p.nombre, COUNT(c.id)) " +
           "FROM Curso c " +
           "JOIN c.profesor p " +
           "GROUP BY p.id, p.nombre")
    List<CursosPorProfesorDTO> findCursosPorProfesor();
    
    // Reporte 2: Nota promedio para cada curso
    @Query("SELECT NEW com.gestionacademica.sistema_academico.dto.reportes.NotaPromedioPorCursoDTO(" +
           "c.nombre, AVG(i.notaFinal)) " +
           "FROM Inscripcion i " +
           "JOIN i.curso c " +
           "WHERE i.notaFinal IS NOT NULL " +
           "GROUP BY c.id, c.nombre")
    List<NotaPromedioPorCursoDTO> findNotaPromedioPorCurso();
    
    // Reporte 3: Estudiantes inscritos por ciclo académico
    @Query("SELECT NEW com.gestionacademica.sistema_academico.dto.reportes.EstudiantesPorCicloDTO(" +
           "i.cicloAcademico, COUNT(DISTINCT i.estudiante.id)) " +
           "FROM Inscripcion i " +
           "GROUP BY i.cicloAcademico " +
           "ORDER BY i.cicloAcademico")
    List<EstudiantesPorCicloDTO> findEstudiantesPorCiclo();
    
    // Reporte 4: Top 3 cursos con nota promedio más alta
    @Query("SELECT NEW com.gestionacademica.sistema_academico.dto.reportes.NotaPromedioPorCursoDTO(" +
           "c.nombre, AVG(i.notaFinal)) " +
           "FROM Inscripcion i " +
           "JOIN i.curso c " +
           "WHERE i.notaFinal IS NOT NULL " +
           "GROUP BY c.id, c.nombre " +
           "ORDER BY AVG(i.notaFinal) DESC")
    List<NotaPromedioPorCursoDTO> findTop3CursosConMejorPromedio();
}