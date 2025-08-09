package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.entity.Estudiante;
import com.gestionacademica.sistema_academico.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstudianteService {
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    /**
     * Crear un nuevo estudiante
     * @param estudiante El estudiante a crear
     * @return El estudiante creado
     * @throws RuntimeException si ya existe un estudiante con el mismo carnet o email
     */
    public Estudiante crearEstudiante(Estudiante estudiante) {
        // Validar datos del estudiante
        validarEstudiante(estudiante);
        
        // Validar que no exista un estudiante con el mismo carnet
        if (estudianteRepository.existsByCarnet(estudiante.getCarnet())) {
            throw new RuntimeException("Ya existe un estudiante con el carnet: " + estudiante.getCarnet());
        }
        
        // Validar que no exista un estudiante con el mismo email
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
     * Obtener todos los estudiantes
     * @return Lista de todos los estudiantes
     */
    @Transactional(readOnly = true)
    public List<Estudiante> obtenerTodos() {
        try {
            return estudianteRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los estudiantes: " + e.getMessage());
        }
    }
    
    /**
     * Obtener un estudiante por su ID
     * @param id ID del estudiante
     * @return Optional con el estudiante si existe
     */
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
    
    /**
     * Actualizar un estudiante existente
     * @param id ID del estudiante a actualizar
     * @param estudianteActualizado Datos actualizados del estudiante
     * @return El estudiante actualizado
     * @throws RuntimeException si el estudiante no existe
     */
    public Estudiante actualizarEstudiante(Long id, Estudiante estudianteActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        // Validar datos del estudiante actualizado
        validarEstudiante(estudianteActualizado);
        
        return estudianteRepository.findById(id)
            .map(estudianteExistente -> {
                // Validar carnet único (solo si cambió)
                if (!estudianteExistente.getCarnet().equals(estudianteActualizado.getCarnet()) &&
                    estudianteRepository.existsByCarnet(estudianteActualizado.getCarnet())) {
                    throw new RuntimeException("Ya existe un estudiante con el carnet: " + estudianteActualizado.getCarnet());
                }
                
                // Validar email único (solo si cambió)
                if (!estudianteExistente.getEmail().equals(estudianteActualizado.getEmail()) &&
                    estudianteRepository.existsByEmail(estudianteActualizado.getEmail())) {
                    throw new RuntimeException("Ya existe un estudiante con el email: " + estudianteActualizado.getEmail());
                }
                
                // Actualizar campos
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
     * @param id ID del estudiante a eliminar
     * @throws RuntimeException si el estudiante no existe
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
    
    /**
     * Buscar estudiante por carnet
     * @param carnet Carnet del estudiante
     * @return Optional con el estudiante si existe
     */
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
    
    /**
     * Buscar estudiante por email
     * @param email Email del estudiante
     * @return Optional con el estudiante si existe
     */
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
    
    /**
     * Buscar estudiantes por nombre o apellido
     * @param termino Término de búsqueda
     * @return Lista de estudiantes que contienen el término
     */
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
    
    /**
     * Buscar estudiantes por año de ingreso
     * @param año Año de ingreso
     * @return Lista de estudiantes que ingresaron en ese año
     */
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
    
    /**
     * Obtener estudiantes ordenados por fecha de ingreso
     * @return Lista de estudiantes ordenados por fecha de ingreso (más recientes primero)
     */
    @Transactional(readOnly = true)
    public List<Estudiante> obtenerOrdenadosPorFechaIngreso() {
        try {
            return estudianteRepository.findAllByOrderByFechaIngresoDesc();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener estudiantes ordenados: " + e.getMessage());
        }
    }
    
    /**
     * Verificar si existe un estudiante con el carnet dado
     * @param carnet Carnet a verificar
     * @return true si existe, false en caso contrario
     */
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
    
    /**
     * Verificar si existe un estudiante con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
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
     * @param estudiante El estudiante a validar
     * @throws IllegalArgumentException si los datos no son válidos
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