package com.gestionacademica.sistema_academico.controller;

import com.gestionacademica.sistema_academico.dto.ProfesorDTO;
import com.gestionacademica.sistema_academico.entity.Profesor;
import com.gestionacademica.sistema_academico.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {
    
    @Autowired
    private ProfesorService profesorService;
    
    // ============ ENDPOINTS CRUD ORIGINALES (Entity) ============
    
    /**
     * POST - Crear un nuevo profesor
     * Endpoint: POST /api/profesores
     */
    @PostMapping
    public ResponseEntity<?> crearProfesor(@Valid @RequestBody Profesor profesor) {
        try {
            Profesor nuevoProfesor = profesorService.crearProfesor(profesor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profesor creado exitosamente");
            response.put("data", nuevoProfesor);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * PUT - Actualizar profesor existente
     * Endpoint: PUT /api/profesores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProfesor(@PathVariable Long id, 
                                               @Valid @RequestBody Profesor profesor) {
        try {
            Profesor profesorActualizado = profesorService.actualizarProfesor(id, profesor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profesor actualizado exitosamente");
            response.put("data", profesorActualizado);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * DELETE - Eliminar profesor por ID
     * Endpoint: DELETE /api/profesores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProfesor(@PathVariable Long id) {
        try {
            profesorService.eliminarProfesor(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profesor eliminado exitosamente");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ NUEVOS ENDPOINTS GET QUE RETORNAN DTOs ============
    
    /**
     * GET - Obtener todos los profesores (DTO)
     * Endpoint: GET /api/profesores
     * Parámetros opcionales: 
     * - ?especialidad=matematicas (filtro por especialidad)
     * - ?experienciaMinima=5 (filtro por años de experiencia mínimos)
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) String especialidad,
                                         @RequestParam(required = false) Integer experienciaMinima) {
        try {
            List<ProfesorDTO> profesores;
            
            // Aplicar filtros si se proporcionan
            if (especialidad != null && !especialidad.trim().isEmpty()) {
                profesores = profesorService.buscarPorEspecialidadDTO(especialidad);
            } else if (experienciaMinima != null) {
                profesores = profesorService.buscarPorAñosExperienciaMinima(experienciaMinima);
            } else {
                profesores = profesorService.obtenerTodosDTO();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profesores obtenidos exitosamente");
            response.put("data", profesores);
            response.put("total", profesores.size());
            
            // Agregar información del filtro aplicado
            if (especialidad != null) {
                response.put("filtroAplicado", "especialidad: " + especialidad);
            } else if (experienciaMinima != null) {
                response.put("filtroAplicado", "experiencia mínima: " + experienciaMinima + " años");
            }
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener profesor por ID (DTO)
     * Endpoint: GET /api/profesores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return profesorService.obtenerPorIdDTO(id)
                .map(profesor -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Profesor encontrado");
                    response.put("data", profesor);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Profesor no encontrado con ID: " + id);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ ENDPOINTS DE FILTRADO ESPECÍFICOS ============
    
    /**
     * GET - Filtrar profesores por especialidad
     * Endpoint: GET /api/profesores/filtro/especialidad
     * Parámetro: ?valor=matematicas
     */
    @GetMapping("/filtro/especialidad")
    public ResponseEntity<?> filtrarPorEspecialidad(@RequestParam String valor) {
        try {
            List<ProfesorDTO> profesores = profesorService.buscarPorEspecialidadDTO(valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por especialidad completado");
            response.put("data", profesores);
            response.put("total", profesores.size());
            response.put("filtroAplicado", "especialidad: " + valor);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Filtrar profesores por experiencia mínima
     * Endpoint: GET /api/profesores/filtro/experiencia
     * Parámetro: ?minima=5
     */
    @GetMapping("/filtro/experiencia")
    public ResponseEntity<?> filtrarPorExperiencia(@RequestParam Integer minima) {
        try {
            List<ProfesorDTO> profesores = profesorService.buscarPorAñosExperienciaMinima(minima);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por experiencia completado");
            response.put("data", profesores);
            response.put("total", profesores.size());
            response.put("filtroAplicado", "experiencia mínima: " + minima + " años");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ ENDPOINTS ADICIONALES DE BÚSQUEDA (Entity - para compatibilidad) ============
    
    /**
     * GET - Buscar profesor por email (endpoint adicional)
     * Endpoint: GET /api/profesores/buscar/email/{email}
     */
    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            return profesorService.buscarPorEmail(email)
                .map(profesor -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Profesor encontrado");
                    response.put("data", profesor);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Profesor no encontrado con email: " + email);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
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