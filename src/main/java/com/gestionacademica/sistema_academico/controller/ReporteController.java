package com.gestionacademica.sistema_academico.controller;

import com.gestionacademica.sistema_academico.dto.reporte.CursosPorProfesorDTO;
import com.gestionacademica.sistema_academico.dto.reporte.EstudiantesPorCicloDTO;
import com.gestionacademica.sistema_academico.dto.reporte.NotaPromedioPorCursoDTO;
import com.gestionacademica.sistema_academico.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {
    
    @Autowired
    private ReporteService reporteService;
    
    /**
     * Endpoint para obtener el número total de cursos que imparte cada profesor
     * GET /api/reportes/cursos-por-profesor
     */
    @GetMapping("/cursos-por-profesor")
    public ResponseEntity<List<CursosPorProfesorDTO>> obtenerCursosPorProfesor() {
        try {
            List<CursosPorProfesorDTO> reporte = reporteService.obtenerCursosPorProfesor();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint para calcular la nota promedio para cada curso
     * GET /api/reportes/nota-promedio-por-curso
     */
    @GetMapping("/nota-promedio-por-curso")
    public ResponseEntity<List<NotaPromedioPorCursoDTO>> obtenerNotaPromedioPorCurso() {
        try {
            List<NotaPromedioPorCursoDTO> reporte = reporteService.obtenerNotaPromedioPorCurso();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint para contar cuántos estudiantes están inscritos por cada ciclo académico
     * GET /api/reportes/estudiantes-por-ciclo
     */
    @GetMapping("/estudiantes-por-ciclo")
    public ResponseEntity<List<EstudiantesPorCicloDTO>> obtenerEstudiantesPorCiclo() {
        try {
            List<EstudiantesPorCicloDTO> reporte = reporteService.obtenerEstudiantesPorCiclo();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint para obtener los 3 cursos con la nota promedio más alta
     * GET /api/reportes/top-3-cursos-mejor-promedio
     */
    @GetMapping("/top-3-cursos-mejor-promedio")
    public ResponseEntity<List<NotaPromedioPorCursoDTO>> obtenerTop3CursosConMejorPromedio() {
        try {
            List<NotaPromedioPorCursoDTO> reporte = reporteService.obtenerTop3CursosConMejorPromedio();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Endpoint adicional para obtener un resumen de todos los reportes
     * GET /api/reportes/resumen
     */
    @GetMapping("/resumen")
    public ResponseEntity<Object> obtenerResumenReportes() {
        try {
            // Crear un objeto con todos los reportes
            var resumen = new Object() {
                public final List<CursosPorProfesorDTO> cursosPorProfesor = reporteService.obtenerCursosPorProfesor();
                public final List<NotaPromedioPorCursoDTO> notaPromedioPorCurso = reporteService.obtenerNotaPromedioPorCurso();
                public final List<EstudiantesPorCicloDTO> estudiantesPorCiclo = reporteService.obtenerEstudiantesPorCiclo();
                public final List<NotaPromedioPorCursoDTO> top3CursosMejorPromedio = reporteService.obtenerTop3CursosConMejorPromedio();
            };
            
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}