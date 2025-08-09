package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.entity.Profesor;
import com.gestionacademica.sistema_academico.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfesorService {
    
    @Autowired
    private ProfesorRepository profesorRepository;
    
    /**
     * Crear un nuevo profesor
     * @param profesor El profesor a crear
     * @return El profesor creado
     * @throws RuntimeException si ya existe un profesor con el mismo email
     */
    public Profesor crearProfesor(Profesor profesor) {
        // Validar que no exista un profesor con el mismo email
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
     * Obtener todos los profesores
     * @return Lista de todos los profesores
     */
    @Transactional(readOnly = true)
    public List<Profesor> obtenerTodos() {
        try {
            return profesorRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los profesores: " + e.getMessage());
        }
    }
    
    /**
     * Obtener un profesor por su ID
     * @param id ID del profesor
     * @return Optional con el profesor si existe
     */
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
    
    /**
     * Actualizar un profesor existente
     * @param id ID del profesor a actualizar
     * @param profesorActualizado Datos actualizados del profesor
     * @return El profesor actualizado
     * @throws RuntimeException si el profesor no existe
     */
    public Profesor actualizarProfesor(Long id, Profesor profesorActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        return profesorRepository.findById(id)
            .map(profesorExistente -> {
                // Validar email único (solo si cambió)
                if (!profesorExistente.getEmail().equals(profesorActualizado.getEmail()) &&
                    profesorRepository.existsByEmail(profesorActualizado.getEmail())) {
                    throw new RuntimeException("Ya existe un profesor con el email: " + profesorActualizado.getEmail());
                }
                
                // Actualizar campos
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
     * @param id ID del profesor a eliminar
     * @throws RuntimeException si el profesor no existe
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
    
    /**
     * Buscar profesores por email
     * @param email Email del profesor
     * @return Optional con el profesor si existe
     */
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
    
    /**
     * Buscar profesores por especialidad
     * @param especialidad Especialidad a buscar
     * @return Lista de profesores con esa especialidad
     */
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
    
    /**
     * Verificar si existe un profesor con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
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