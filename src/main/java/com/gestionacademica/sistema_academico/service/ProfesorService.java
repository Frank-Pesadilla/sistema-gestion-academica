package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.dto.ProfesorDTO;
import com.gestionacademica.sistema_academico.entity.Profesor;
import com.gestionacademica.sistema_academico.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfesorService {
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    // ============ MÉTODOS CRUD ORIGINALES (mantener para POST, PUT, DELETE) ============
    
    /**
     * Crear un nuevo profesor
     */
    public Profesor crearProfesor(Profesor profesor) {
        if (profesorRepository.existsByEmail(profesor.getEmail())) {
            throw new RuntimeException("Ya existe un profesor con el email: " + profesor.getEmail());
        }
        
        try {
            return profesorRepository.save(profesor);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el profesor: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar un profesor existente
     */
    public Profesor actualizarProfesor(Long id, Profesor profesorActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        return profesorRepository.findById(id)
            .map(profesorExistente -> {
                if (!profesorExistente.getEmail().equals(profesorActualizado.getEmail()) &&
                    profesorRepository.existsByEmail(profesorActualizado.getEmail())) {
                    throw new RuntimeException("Ya existe un profesor con el email: " + profesorActualizado.getEmail());
                }
                
                profesorExistente.setNombre(profesorActualizado.getNombre());
                profesorExistente.setApellido(profesorActualizado.getApellido());
                profesorExistente.setEmail(profesorActualizado.getEmail());
                profesorExistente.setTelefono(profesorActualizado.getTelefono());
                profesorExistente.setEspecialidad(profesorActualizado.getEspecialidad());
                profesorExistente.setFechaContratacion(profesorActualizado.getFechaContratacion());
                
                try {
                    return profesorRepository.save(profesorExistente);
                } catch (Exception e) {
                    throw new RuntimeException("Error al actualizar el profesor: " + e.getMessage());
                }
            })
            .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + id));
    }
    
    /**
     * Eliminar un profesor por su ID
     */
    public void eliminarProfesor(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        if (!profesorRepository.existsById(id)) {
            throw new RuntimeException("Profesor no encontrado con ID: " + id);
        }
        
        try {
            profesorRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el profesor: " + e.getMessage());
        }
    }
    
    // ============ NUEVOS MÉTODOS QUE RETORNAN DTOs ============
    
    /**
     * Obtener todos los profesores como DTOs
     */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> obtenerTodosDTO() {
        try {
            List<Profesor> profesores = profesorRepository.findAll();
            return profesores.stream()
                    .map(this::convertirAProfesorDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los profesores: " + e.getMessage());
        }
    }
    
    /**
     * Obtener un profesor por ID como DTO
     */
    @Transactional(readOnly = true)
    public Optional<ProfesorDTO> obtenerPorIdDTO(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try {
            return profesorRepository.findById(id)
                    .map(this::convertirAProfesorDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el profesor: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 1: Buscar profesores por especialidad como DTOs
     */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> buscarPorEspecialidadDTO(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad no puede estar vacía");
        }
        
        try {
            List<Profesor> profesores = profesorRepository.findByEspecialidadContainingIgnoreCase(especialidad.trim());
            return profesores.stream()
                    .map(this::convertirAProfesorDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesores por especialidad: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 2: Buscar profesores por años de experiencia mínimos
     */
    @Transactional(readOnly = true)
    public List<ProfesorDTO> buscarPorAñosExperienciaMinima(Integer añosMinimos) {
        if (añosMinimos == null || añosMinimos < 0) {
            throw new IllegalArgumentException("Los años mínimos deben ser un número positivo");
        }
        
        try {
            List<Profesor> profesores = profesorRepository.findAll();
            return profesores.stream()
                    .filter(profesor -> {
                        if (profesor.getFechaContratacion() == null) return false;
                        int añosExp = Period.between(profesor.getFechaContratacion(), LocalDate.now()).getYears();
                        return añosExp >= añosMinimos;
                    })
                    .map(this::convertirAProfesorDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesores por experiencia: " + e.getMessage());
        }
    }
    
    // ============ MÉTODOS DE CONVERSIÓN PRIVADOS ============
    
    /**
     * Convierte una entidad Profesor a ProfesorDTO
     */
    private ProfesorDTO convertirAProfesorDTO(Profesor profesor) {
        if (profesor == null) {
            return null;
        }
        
        // Combinar nombre y apellido
        String nombreCompleto = profesor.getNombre() + " " + profesor.getApellido();
        
        // Calcular años de experiencia
        Integer añosExperiencia = 0;
        if (profesor.getFechaContratacion() != null) {
            añosExperiencia = Period.between(profesor.getFechaContratacion(), LocalDate.now()).getYears();
        }
        
        return new ProfesorDTO(
            profesor.getId(),
            nombreCompleto,
            profesor.getEmail(),
            profesor.getTelefono(),
            profesor.getEspecialidad(),
            profesor.getFechaContratacion(),
            añosExperiencia
        );
    }
    
    // ============ MÉTODOS ORIGINALES PARA COMPATIBILIDAD ============
    
    @Transactional(readOnly = true)
    public List<Profesor> obtenerTodos() {
        try {
            return profesorRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los profesores: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Profesor> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try {
            return profesorRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el profesor: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Profesor> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        try {
            return profesorRepository.findByEmail(email.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el profesor por email: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Profesor> buscarPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad no puede estar vacía");
        }
        
        try {
            return profesorRepository.findByEspecialidadContainingIgnoreCase(especialidad.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar profesores por especialidad: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            return profesorRepository.existsByEmail(email.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el email: " + e.getMessage());
        }
    }
}