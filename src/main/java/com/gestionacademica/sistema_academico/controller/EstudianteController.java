package com.gestionacademica.sistema_academico.controller;

import com.gestionacademica.sistema_academico.dto.EstudianteDTO;
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
    
    // ============ ENDPOINTS CRUD ORIGINALES (Entity) ============
    
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
    
    // ============ NUEVOS ENDPOINTS GET QUE RETORNAN DTOs ============
    
    /**
     * GET - Obtener todos los estudiantes (DTO)
     * Endpoint: GET /api/estudiantes
     * Parámetros opcionales: 
     * - ?apellido=gonzalez (filtro por apellido)
     * - ?semestre=5 (filtro por semestre actual)
     * - ?edadMinima=18&edadMaxima=25 (filtro por rango de edad)
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos(@RequestParam(required = false) String apellido,
                                         @RequestParam(required = false) String semestre,
                                         @RequestParam(required = false) Integer edadMinima,
                                         @RequestParam(required = false) Integer edadMaxima) {
        try {
            List<EstudianteDTO> estudiantes;
            String filtroAplicado = null;
            
            // Aplicar filtros si se proporcionan (prioridad: apellido > semestre > rango edad)
            if (apellido != null && !apellido.trim().isEmpty()) {
                estudiantes = estudianteService.buscarPorApellidoDTO(apellido);
                filtroAplicado = "apellido: " + apellido;
            } else if (semestre != null && !semestre.trim().isEmpty()) {
                estudiantes = estudianteService.buscarPorSemestreDTO(semestre);
                filtroAplicado = "semestre: " + semestre;
            } else if (edadMinima != null && edadMaxima != null) {
                estudiantes = estudianteService.buscarPorRangoEdadDTO(edadMinima, edadMaxima);
                filtroAplicado = "edad entre " + edadMinima + " y " + edadMaxima + " años";
            } else {
                estudiantes = estudianteService.obtenerTodosDTO();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estudiantes obtenidos exitosamente");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            
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
     * GET - Obtener estudiante por ID (DTO)
     * Endpoint: GET /api/estudiantes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return estudianteService.obtenerPorIdDTO(id)
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
    
    // ============ ENDPOINTS DE FILTRADO ESPECÍFICOS ============
    
    /**
     * GET - Filtrar estudiantes por apellido
     * Endpoint: GET /api/estudiantes/filtro/apellido
     * Parámetro: ?valor=gonzalez
     */
    @GetMapping("/filtro/apellido")
    public ResponseEntity<?> filtrarPorApellido(@RequestParam String valor) {
        try {
            List<EstudianteDTO> estudiantes = estudianteService.buscarPorApellidoDTO(valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por apellido completado");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            response.put("filtroAplicado", "apellido: " + valor);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Filtrar estudiantes por semestre
     * Endpoint: GET /api/estudiantes/filtro/semestre
     * Parámetro: ?valor=5° Semestre
     */
    @GetMapping("/filtro/semestre")
    public ResponseEntity<?> filtrarPorSemestre(@RequestParam String valor) {
        try {
            List<EstudianteDTO> estudiantes = estudianteService.buscarPorSemestreDTO(valor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por semestre completado");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            response.put("filtroAplicado", "semestre: " + valor);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    /**
     * GET - Filtrar estudiantes por rango de edad
     * Endpoint: GET /api/estudiantes/filtro/edad
     * Parámetros: ?minima=18&maxima=25
     */
    @GetMapping("/filtro/edad")
    public ResponseEntity<?> filtrarPorRangoEdad(@RequestParam Integer minima, 
                                                @RequestParam Integer maxima) {
        try {
            List<EstudianteDTO> estudiantes = estudianteService.buscarPorRangoEdadDTO(minima, maxima);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Filtrado por rango de edad completado");
            response.put("data", estudiantes);
            response.put("total", estudiantes.size());
            response.put("filtroAplicado", "edad entre " + minima + " y " + maxima + " años");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            return handleException(e);
        }
    }
    
    // ============ ENDPOINTS ADICIONALES DE BÚSQUEDA (Entity - para compatibilidad) ============
    
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