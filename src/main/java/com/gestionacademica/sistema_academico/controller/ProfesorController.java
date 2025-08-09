package com.gestionacademica.sistema_academico.controller;

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
     * GET - Obtener todos los profesores
     * Endpoint: GET /api/profesores
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Profesor> profesores = profesorService.obtenerTodos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profesores obtenidos exitosamente");
            response.put("data", profesores);
            response.put("total", profesores.size());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener profesor por ID
     * Endpoint: GET /api/profesores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return profesorService.obtenerPorId(id)
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
     * GET - Buscar profesores por especialidad (endpoint adicional)
     * Endpoint: GET /api/profesores/buscar/especialidad/{especialidad}
     */
    @GetMapping("/buscar/especialidad/{especialidad}")
    public ResponseEntity<?> buscarPorEspecialidad(@PathVariable String especialidad) {
        try {
            List<Profesor> profesores = profesorService.buscarPorEspecialidad(especialidad);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Búsqueda completada");
            response.put("data", profesores);
            response.put("total", profesores.size());
            
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