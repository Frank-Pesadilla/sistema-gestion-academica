package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.entity.Curso;
import com.gestionacademica.sistema_academico.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;
    
    /**
     * Crear un nuevo curso
     * @param curso El curso a crear
     * @return El curso creado
     * @throws RuntimeException si ya existe un curso con el mismo código
     */
    public Curso crearCurso(Curso curso) {
        // Validar que no exista un curso con el mismo código
        if (cursoRepository.existsByCodigo(curso.getCodigo())) {
            throw new RuntimeException("Ya existe un curso con el código: " + curso.getCodigo());
        }
        
        // Validaciones adicionales
        validarCurso(curso);
        
        try {
            return cursoRepository.save(curso);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el curso: " + e.getMessage());
        }
    }
    
    /**
     * Obtener todos los cursos
     * @return Lista de todos los cursos
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        try {
            return cursoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los cursos: " + e.getMessage());
        }
    }
    
    /**
     * Obtener un curso por su ID
     * @param id ID del curso
     * @return Optional con el curso si existe
     */
    @Transactional(readOnly = true)
    public Optional<Curso> obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try {
            return cursoRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el curso: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar un curso existente
     * @param id ID del curso a actualizar
     * @param cursoActualizado Datos actualizados del curso
     * @return El curso actualizado
     * @throws RuntimeException si el curso no existe
     */
    public Curso actualizarCurso(Long id, Curso cursoActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        // Validar datos del curso actualizado
        validarCurso(cursoActualizado);
        
        return cursoRepository.findById(id)
            .map(cursoExistente -> {
                // Validar código único (solo si cambió)
                if (!cursoExistente.getCodigo().equals(cursoActualizado.getCodigo()) &&
                    cursoRepository.existsByCodigo(cursoActualizado.getCodigo())) {
                    throw new RuntimeException("Ya existe un curso con el código: " + cursoActualizado.getCodigo());
                }
                
                // Actualizar campos
                cursoExistente.setCodigo(cursoActualizado.getCodigo());
                cursoExistente.setNombre(cursoActualizado.getNombre());
                cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
                cursoExistente.setCreditos(cursoActualizado.getCreditos());
                cursoExistente.setHorasSemanales(cursoActualizado.getHorasSemanales());
                
                try {
                    return cursoRepository.save(cursoExistente);
                } catch (Exception e) {
                    throw new RuntimeException("Error al actualizar el curso: " + e.getMessage());
                }
            })
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }
    
    /**
     * Eliminar un curso por su ID
     * @param id ID del curso a eliminar
     * @throws RuntimeException si el curso no existe
     */
    public void eliminarCurso(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
        
        try {
            cursoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el curso: " + e.getMessage());
        }
    }
    
    /**
     * Buscar curso por código
     * @param codigo Código del curso
     * @return Optional con el curso si existe
     */
    @Transactional(readOnly = true)
    public Optional<Curso> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío");
        }
        
        try {
            return cursoRepository.findByCodigo(codigo.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el curso por código: " + e.getMessage());
        }
    }
    
    /**
     * Buscar cursos por nombre
     * @param nombre Nombre del curso (búsqueda parcial)
     * @return Lista de cursos que contienen el nombre
     */
    @Transactional(readOnly = true)
    public List<Curso> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        try {
            return cursoRepository.findByNombreContainingIgnoreCase(nombre.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por nombre: " + e.getMessage());
        }
    }
    
    /**
     * Buscar cursos por número de créditos
     * @param creditos Número de créditos
     * @return Lista de cursos con ese número de créditos
     */
    @Transactional(readOnly = true)
    public List<Curso> buscarPorCreditos(Integer creditos) {
        if (creditos == null || creditos <= 0) {
            throw new IllegalArgumentException("Los créditos deben ser un número positivo");
        }
        
        try {
            return cursoRepository.findByCreditos(creditos);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por créditos: " + e.getMessage());
        }
    }
    
    /**
     * Obtener cursos ordenados por créditos (ascendente)
     * @return Lista de cursos ordenados por créditos
     */
    @Transactional(readOnly = true)
    public List<Curso> obtenerOrdenadosPorCreditos() {
        try {
            return cursoRepository.findAllByOrderByCreditosAsc();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cursos ordenados: " + e.getMessage());
        }
    }
    
    /**
     * Verificar si existe un curso con el código dado
     * @param codigo Código a verificar
     * @return true si existe, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean existePorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        
        try {
            return cursoRepository.existsByCodigo(codigo.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el código: " + e.getMessage());
        }
    }
    
    /**
     * Validar los datos de un curso
     * @param curso El curso a validar
     * @throws IllegalArgumentException si los datos no son válidos
     */
    private void validarCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("El curso no puede ser nulo");
        }
        
        if (curso.getCodigo() == null || curso.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del curso es obligatorio");
        }
        
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio");
        }
        
        if (curso.getCreditos() == null || curso.getCreditos() <= 0 || curso.getCreditos() > 10) {
            throw new IllegalArgumentException("Los créditos deben estar entre 1 y 10");
        }
        
        if (curso.getHorasSemanales() != null && (curso.getHorasSemanales() <= 0 || curso.getHorasSemanales() > 20)) {
            throw new IllegalArgumentException("Las horas semanales deben estar entre 1 y 20");
        }
        
        // Normalizar código a mayúsculas
        curso.setCodigo(curso.getCodigo().trim().toUpperCase());
    }
}