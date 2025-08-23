package com.gestionacademica.sistema_academico.controller;

import com.gestionacademica.sistema_academico.dto.CursoDTO;
import com.gestionacademica.sistema_academico.entity.Curso;
import com.gestionacademica.sistema_academico.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {
    
    @Autowired
    private CursoService cursoService;
    
    // ============ ENDPOINTS CRUD ORIGINALES (Entity) ============
    
    /**
     * POST - Crear un nuevo curso
     * Endpoint: POST /api/cursos
     */
    @PostMapping
    public ResponseEntity<?> crearCurso(@Valid @RequestBody Curso curso) {
        try {
            Curso nuevoCurso = cursoService.crearCurso(curso);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Curso creado exitosamente");
            response.put("data", nuevoCurso);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * PUT - Actualizar curso existente
     * Endpoint: PUT /api/cursos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCurso(@PathVariable Long id, 
                                            @Valid @RequestBody Curso curso) {
        try {
            Curso cursoActualizado = cursoService.actualizarCurso(id, curso);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Curso actualizado exitosamente");
            response.put("data", cursoActualizado);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * DELETE - Eliminar curso por ID
     * Endpoint: DELETE /api/cursos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id) {
        try {
            cursoService.eliminarCurso(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Curso eliminado exitosamente");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ NUEVOS ENDPOINTS GET QUE RETORNAN DTOs ============
    
    /**
     * GET - Obtener todos los cursos (DTO)
     * Endpoint: GET /api/cursos
     * Parámetros opcionales: 
     * - ?creditos=4 (filtro por número de créditos)
     * - ?nivel=basico (filtro por nivel de dificultad)
     * - ?carga=media (filtro por carga académica)
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) Integer creditos,
                                         @RequestParam(required = false) String nivel,
                                         @RequestParam(required = false) String carga) {
        try {
            List<CursoDTO> cursos;
            String filtroAplicado = null;
            
            // Aplicar filtros si se proporcionan (prioridad: creditos > nivel > carga)
            if (creditos != null) {
                cursos = cursoService.buscarPorCreditosDTO(creditos);
                filtroAplicado = "créditos: " + creditos;
            } else if (nivel != null && !nivel.trim().isEmpty()) {
                cursos = cursoService.buscarPorNivelDificultadDTO(nivel);
                filtroAplicado = "nivel: " + nivel;
            } else if (carga != null && !carga.trim().isEmpty()) {
                cursos = cursoService.buscarPorCargaAcademicaDTO(carga);
                filtroAplicado = "carga académica: " + carga;
            } else {
                cursos = cursoService.obtenerTodosDTO();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cursos obtenidos exitosamente");
            response.put("data", cursos);
            response.put("total", cursos.size());
            
            // Agregar información del filtro aplicado
            if (filtroAplicado != null) {
                response.put("filtroAplicado", filtroAplicado);
            }
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener curso por ID (DTO)
     * Endpoint: GET /api/cursos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return cursoService.obtenerPorIdDTO(id)
                .map(curso -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Curso encontrado");
                    response.put("data", curso);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Curso no encontrado con ID: " + id);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ ENDPOINTS DE FILTRADO ESPECÍFICOS ============
    
    /**
     * GET - Filtrar cursos por créditos
     * Endpoint: GET /api/cursos/filtro/creditos
     * Parámetro: ?valor=4
     */
    @GetMapping("/filtro/creditos")
    public ResponseEntity<?> filtrarPorCreditos(@RequestParam Integer valor) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorCreditosDTO(valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por créditos completado");
            response.put("data", cursos);
            response.put("total", cursos.size());
            response.put("filtroAplicado", "créditos: " + valor);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Filtrar cursos por nivel de dificultad
     * Endpoint: GET /api/cursos/filtro/nivel
     * Parámetro: ?valor=basico (valores: basico, intermedio, avanzado)
     */
    @GetMapping("/filtro/nivel")
    public ResponseEntity<?> filtrarPorNivel(@RequestParam String valor) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorNivelDificultadDTO(valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por nivel de dificultad completado");
            response.put("data", cursos);
            response.put("total", cursos.size());
            response.put("filtroAplicado", "nivel: " + valor);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Filtrar cursos por carga académica
     * Endpoint: GET /api/cursos/filtro/carga
     * Parámetro: ?valor=media (valores: baja, media, alta)
     */
    @GetMapping("/filtro/carga")
    public ResponseEntity<?> filtrarPorCargaAcademica(@RequestParam String valor) {
        try {
            List<CursoDTO> cursos = cursoService.buscarPorCargaAcademicaDTO(valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por carga académica completado");
            response.put("data", cursos);
            response.put("total", cursos.size());
            response.put("filtroAplicado", "carga académica: " + valor);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ ENDPOINTS ADICIONALES DE BÚSQUEDA (Entity - para compatibilidad) ============
    
    /**
     * GET - Buscar curso por código (endpoint adicional)
     * Endpoint: GET /api/cursos/buscar/codigo/{codigo}
     */
    @GetMapping("/buscar/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        try {
            return cursoService.buscarPorCodigo(codigo)
                .map(curso -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Curso encontrado");
                    response.put("data", curso);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Curso no encontrado con código: " + codigo);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener cursos ordenados por créditos (endpoint adicional)
     * Endpoint: GET /api/cursos/ordenados/creditos
     */
    @GetMapping("/ordenados/creditos")
    public ResponseEntity<?> obtenerOrdenadosPorCreditos() {
        try {
            List<Curso> cursos = cursoService.obtenerOrdenadosPorCreditos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cursos ordenados por créditos");
            response.put("data", cursos);
            response.put("total", cursos.size());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * Maneja todas las excepciones de manera centralizada
     */
    private ResponseEntity<?> handleException(Exception e) {
        HttpStatus status;
        String errorType;
        String message = e.getMessage();
        
        // Determinar el tipo de error y status code apropiado
        if (e instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            errorType = "BAD_REQUEST";
        } else if (e instanceof RuntimeException && message.contains("no encontrado")) {
            status = HttpStatus.NOT_FOUND;
            errorType = "NOT_FOUND";
        } else if (e instanceof RuntimeException) {
            status = HttpStatus.BAD_REQUEST;
            errorType = "BAD_REQUEST";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorType = "INTERNAL_SERVER_ERROR";
            message = "Error interno del servidor";
        }
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("error", errorType);
        
        return new ResponseEntity<>(errorResponse, status);
    }
}