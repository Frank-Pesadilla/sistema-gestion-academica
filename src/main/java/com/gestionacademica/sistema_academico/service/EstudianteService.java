package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.dto.EstudianteDTO;
import com.gestionacademica.sistema_academico.entity.Estudiante;
import com.gestionacademica.sistema_academico.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EstudianteService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    // ============ MÉTODOS CRUD ORIGINALES (mantener para POST, PUT, DELETE) ============
    
    /**
     * Crear un nuevo estudiante
     */
    public Estudiante crearEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);
        
        if (estudianteRepository.existsByCarnet(estudiante.getCarnet())) {
            throw new RuntimeException("Ya existe un estudiante con el carnet: " + estudiante.getCarnet());
        }
        
        if (estudianteRepository.existsByEmail(estudiante.getEmail())) {
            throw new RuntimeException("Ya existe un estudiante con el email: " + estudiante.getEmail());
        }
        
        try {
            return estudianteRepository.save(estudiante);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el estudiante: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar un estudiante existente
     */
    public Estudiante actualizarEstudiante(Long id, Estudiante estudianteActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        validarEstudiante(estudianteActualizado);
        
        return estudianteRepository.findById(id)
            .map(estudianteExistente -> {
                if (!estudianteExistente.getCarnet().equals(estudianteActualizado.getCarnet()) &&
                    estudianteRepository.existsByCarnet(estudianteActualizado.getCarnet())) {
                    throw new RuntimeException("Ya existe un estudiante con el carnet: " + estudianteActualizado.getCarnet());
                }
                
                if (!estudianteExistente.getEmail().equals(estudianteActualizado.getEmail()) &&
                    estudianteRepository.existsByEmail(estudianteActualizado.getEmail())) {
                    throw new RuntimeException("Ya existe un estudiante con el email: " + estudianteActualizado.getEmail());
                }
                
                estudianteExistente.setCarnet(estudianteActualizado.getCarnet());
                estudianteExistente.setNombre(estudianteActualizado.getNombre());
                estudianteExistente.setApellido(estudianteActualizado.getApellido());
                estudianteExistente.setEmail(estudianteActualizado.getEmail());
                estudianteExistente.setTelefono(estudianteActualizado.getTelefono());
                estudianteExistente.setFechaNacimiento(estudianteActualizado.getFechaNacimiento());
                estudianteExistente.setFechaIngreso(estudianteActualizado.getFechaIngreso());
                
                try {
                    return estudianteRepository.save(estudianteExistente);
                } catch (Exception e) {
                    throw new RuntimeException("Error al actualizar el estudiante: " + e.getMessage());
                }
            })
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + id));
    }
    
    /**
     * Eliminar un estudiante por su ID
     */
    public void eliminarEstudiante(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        if (!estudianteRepository.existsById(id)) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + id);
        }
        
        try {
            estudianteRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el estudiante: " + e.getMessage());
        }
    }
    
    // ============ NUEVOS MÉTODOS QUE RETORNAN DTOs ============
    
    /**
     * Obtener todos los estudiantes como DTOs
     */
    @Transactional(readOnly = true)
    public List<EstudianteDTO> obtenerTodosDTO() {
        try {
            List<Estudiante> estudiantes = estudianteRepository.findAll();
            return estudiantes.stream()
                    .map(this::convertirAEstudianteDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los estudiantes: " + e.getMessage());
        }
    }
    
    /**
     * Obtener un estudiante por ID como DTO
     */
    @Transactional(readOnly = true)
    public Optional<EstudianteDTO> obtenerPorIdDTO(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try {
            return estudianteRepository.findById(id)
                    .map(this::convertirAEstudianteDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el estudiante: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 1: Buscar estudiantes por apellido como DTOs
     */
    @Transactional(readOnly = true)
    public List<EstudianteDTO> buscarPorApellidoDTO(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        try {
            List<Estudiante> estudiantes = estudianteRepository.findByNombreOrApellidoContaining(apellido.trim());
            return estudiantes.stream()
                    .filter(est -> est.getApellido().toLowerCase().contains(apellido.toLowerCase()))
                    .map(this::convertirAEstudianteDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiantes por apellido: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 2: Buscar estudiantes por semestre actual
     */
    @Transactional(readOnly = true)
    public List<EstudianteDTO> buscarPorSemestreDTO(String semestre) {
        if (semestre == null || semestre.trim().isEmpty()) {
            throw new IllegalArgumentException("El semestre no puede estar vacío");
        }
        
        try {
            List<Estudiante> estudiantes = estudianteRepository.findAll();
            return estudiantes.stream()
                    .map(this::convertirAEstudianteDTO)
                    .filter(estDTO -> estDTO.getSemestreActual().equalsIgnoreCase(semestre.trim()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiantes por semestre: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 3: Buscar estudiantes por rango de edad
     */
    @Transactional(readOnly = true)
    public List<EstudianteDTO> buscarPorRangoEdadDTO(Integer edadMinima, Integer edadMaxima) {
        if (edadMinima == null || edadMaxima == null || edadMinima < 0 || edadMaxima < 0 || edadMinima > edadMaxima) {
            throw new IllegalArgumentException("El rango de edad debe ser válido");
        }
        
        try {
            List<Estudiante> estudiantes = estudianteRepository.findAll();
            return estudiantes.stream()
                    .map(this::convertirAEstudianteDTO)
                    .filter(estDTO -> {
                        Integer edad = estDTO.getEdad();
                        return edad != null && edad >= edadMinima && edad <= edadMaxima;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiantes por rango de edad: " + e.getMessage());
        }
    }
    
    // ============ MÉTODOS DE CONVERSIÓN PRIVADOS ============
    
    /**
     * Convierte una entidad Estudiante a EstudianteDTO
     */
    private EstudianteDTO convertirAEstudianteDTO(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }
        
        // Combinar nombre y apellido
        String nombreCompleto = estudiante.getNombre() + " " + estudiante.getApellido();
        
        // Calcular edad
        Integer edad = null;
        if (estudiante.getFechaNacimiento() != null) {
            edad = Period.between(estudiante.getFechaNacimiento(), LocalDate.now()).getYears();
        }
        
        // Calcular semestre actual
        String semestreActual = calcularSemestreActual(estudiante.getFechaIngreso());
        
        // Estado académico (por ahora siempre activo, se puede expandir)
        String estadoAcademico = "Activo";
        
        return new EstudianteDTO(
            estudiante.getId(),
            estudiante.getCarnet(),
            nombreCompleto,
            estudiante.getEmail(),
            estudiante.getTelefono(),
            edad,
            estudiante.getFechaIngreso(),
            semestreActual,
            estadoAcademico
        );
    }
    
    /**
     * Calcula el semestre actual basado en la fecha de ingreso
     */
    private String calcularSemestreActual(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            return "Sin determinar";
        }
        
        LocalDate ahora = LocalDate.now();
        long mesesTranscurridos = ChronoUnit.MONTHS.between(fechaIngreso, ahora);
        
        // Cada semestre son aproximadamente 6 meses
        int semestre = (int) (mesesTranscurridos / 6) + 1;
        
        // Limitar a un máximo razonable
        if (semestre > 12) {
            semestre = 12;
        } else if (semestre < 1) {
            semestre = 1;
        }
        
        return semestre + "° Semestre";
    }
    
    // ============ MÉTODOS ORIGINALES PARA COMPATIBILIDAD ============
    
    @Transactional(readOnly = true)
    public List<Estudiante> obtenerTodos() {
        try {
            return estudianteRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los estudiantes: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Estudiante> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try {
            return estudianteRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el estudiante: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Estudiante> buscarPorCarnet(String carnet) {
        if (carnet == null || carnet.trim().isEmpty()) {
            throw new IllegalArgumentException("El carnet no puede estar vacío");
        }
        
        try {
            return estudianteRepository.findByCarnet(carnet.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el estudiante por carnet: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Estudiante> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        try {
            return estudianteRepository.findByEmail(email.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el estudiante por email: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Estudiante> buscarPorNombreOApellido(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        
        try {
            return estudianteRepository.findByNombreOrApellidoContaining(termino.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiantes por nombre o apellido: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Estudiante> buscarPorAñoIngreso(int año) {
        if (año < 1900 || año > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("El año debe estar entre 1900 y " + (LocalDate.now().getYear() + 1));
        }
        
        try {
            return estudianteRepository.findByAñoIngreso(año);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar estudiantes por año de ingreso: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Estudiante> obtenerOrdenadosPorFechaIngreso() {
        try {
            return estudianteRepository.findAllByOrderByFechaIngresoDesc();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estudiantes ordenados: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public boolean existePorCarnet(String carnet) {
        if (carnet == null || carnet.trim().isEmpty()) {
            return false;
        }
        
        try {
            return estudianteRepository.existsByCarnet(carnet.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el carnet: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            return estudianteRepository.existsByEmail(email.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el email: " + e.getMessage());
        }
    }
    
    /**
     * Validar los datos de un estudiante
     */
    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo");
        }
        
        if (estudiante.getCarnet() == null || estudiante.getCarnet().trim().isEmpty()) {
            throw new IllegalArgumentException("El carnet del estudiante es obligatorio");
        }
        
        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estudiante es obligatorio");
        }
        
        if (estudiante.getApellido() == null || estudiante.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del estudiante es obligatorio");
        }
        
        if (estudiante.getEmail() == null || estudiante.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del estudiante es obligatorio");
        }
        
        // Validar formato de email básico
        if (!estudiante.getEmail().contains("@") || !estudiante.getEmail().contains(".")) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        
        // Validar fechas
        if (estudiante.getFechaNacimiento() != null && estudiante.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
        
        if (estudiante.getFechaIngreso() != null && estudiante.getFechaIngreso().isAfter(LocalDate.now().plusDays(30))) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser muy futura");
        }
        
        // Normalizar carnet a mayúsculas
        estudiante.setCarnet(estudiante.getCarnet().trim().toUpperCase());
    }
}