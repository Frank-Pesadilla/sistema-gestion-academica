package com.gestionacademica.sistema_academico.controller;

import com.gestionacademica.sistema_academico.entity.Estudiante;
import com.gestionacademica.sistema_academico.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "*")
public class EstudianteController {
    
    @Autowired
    private EstudianteService estudianteService;
    
    /**
     * POST - Crear un nuevo estudiante
     * Endpoint: POST /api/estudiantes
     */
    @PostMapping
    public ResponseEntity<?> crearEstudiante(@Valid @RequestBody Estudiante estudiante) {
        try {
            Estudiante nuevoEstudiante = estudianteService.crearEstudiante(estudiante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estudiante creado exitosamente");
            response.put("data", nuevoEstudiante);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener todos los estudiantes
     * Endpoint: GET /api/estudiantes
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerTodos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estudiantes obtenidos exitosamente");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener estudiante por ID
     * Endpoint: GET /api/estudiantes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return estudianteService.obtenerPorId(id)
                .map(estudiante -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Estudiante encontrado");
                    response.put("data", estudiante);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Estudiante no encontrado con ID: " + id);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * PUT - Actualizar estudiante existente
     * Endpoint: PUT /api/estudiantes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstudiante(@PathVariable Long id, 
                                                 @Valid @RequestBody Estudiante estudiante) {
        try {
            Estudiante estudianteActualizado = estudianteService.actualizarEstudiante(id, estudiante);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estudiante actualizado exitosamente");
            response.put("data", estudianteActualizado);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * DELETE - Eliminar estudiante por ID
     * Endpoint: DELETE /api/estudiantes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Long id) {
        try {
            estudianteService.eliminarEstudiante(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estudiante eliminado exitosamente");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Buscar estudiante por carnet (endpoint adicional)
     * Endpoint: GET /api/estudiantes/buscar/carnet/{carnet}
     */
    @GetMapping("/buscar/carnet/{carnet}")
    public ResponseEntity<?> buscarPorCarnet(@PathVariable String carnet) {
        try {
            return estudianteService.buscarPorCarnet(carnet)
                .map(estudiante -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Estudiante encontrado");
                    response.put("data", estudiante);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Estudiante no encontrado con carnet: " + carnet);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Buscar estudiante por email (endpoint adicional)
     * Endpoint: GET /api/estudiantes/buscar/email/{email}
     */
    @GetMapping("/buscar/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            return estudianteService.buscarPorEmail(email)
                .map(estudiante -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "Estudiante encontrado");
                    response.put("data", estudiante);
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Estudiante no encontrado con email: " + email);
                    errorResponse.put("error", "NOT_FOUND");
                    
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                });
                
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Buscar estudiantes por año de ingreso (endpoint adicional)
     * Endpoint: GET /api/estudiantes/buscar/año/{año}
     */
    @GetMapping("/buscar/año/{año}")
    public ResponseEntity<?> buscarPorAñoIngreso(@PathVariable int año) {
        try {
            List<Estudiante> estudiantes = estudianteService.buscarPorAñoIngreso(año);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Búsqueda completada");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Obtener estudiantes ordenados por fecha de ingreso (endpoint adicional)
     * Endpoint: GET /api/estudiantes/ordenados/fecha-ingreso
     */
    @GetMapping("/ordenados/fecha-ingreso")
    public ResponseEntity<?> obtenerOrdenadosPorFechaIngreso() {
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerOrdenadosPorFechaIngreso();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estudiantes ordenados por fecha de ingreso");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            
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