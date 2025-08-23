package com.gestionacademica.sistema_academico.service;

import com.gestionacademica.sistema_academico.dto.CursoDTO;
import com.gestionacademica.sistema_academico.entity.Curso;
import com.gestionacademica.sistema_academico.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CursoService {
    
    @Autowired
    private CursoRepository cursoRepository;
    
    // ============ MÉTODOS CRUD ORIGINALES (mantener para POST, PUT, DELETE) ============
    
    /**
     * Crear un nuevo curso
     */
    public Curso crearCurso(Curso curso) {
        if (cursoRepository.existsByCodigo(curso.getCodigo())) {
            throw new RuntimeException("Ya existe un curso con el código: " + curso.getCodigo());
        }
        
        validarCurso(curso);
        
        try {
            return cursoRepository.save(curso);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el curso: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar un curso existente
     */
    public Curso actualizarCurso(Long id, Curso cursoActualizado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        validarCurso(cursoActualizado);
        
        return cursoRepository.findById(id)
            .map(cursoExistente -> {
                if (!cursoExistente.getCodigo().equals(cursoActualizado.getCodigo()) &&
                    cursoRepository.existsByCodigo(cursoActualizado.getCodigo())) {
                    throw new RuntimeException("Ya existe un curso con el código: " + cursoActualizado.getCodigo());
                }
                
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
    
    // ============ NUEVOS MÉTODOS QUE RETORNAN DTOs ============
    
    /**
     * Obtener todos los cursos como DTOs
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> obtenerTodosDTO() {
        try {
            List<Curso> cursos = cursoRepository.findAll();
            return cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los cursos: " + e.getMessage());
        }
    }
    
    /**
     * Obtener un curso por ID como DTO
     */
    @Transactional(readOnly = true)
    public Optional<CursoDTO> obtenerPorIdDTO(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        
        try {
            return cursoRepository.findById(id)
                    .map(this::convertirACursoDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el curso: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 1: Buscar cursos por número de créditos como DTOs
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorCreditosDTO(Integer creditos) {
        if (creditos == null || creditos <= 0) {
            throw new IllegalArgumentException("Los créditos deben ser un número positivo");
        }
        
        try {
            List<Curso> cursos = cursoRepository.findByCreditos(creditos);
            return cursos.stream()
                    .map(this::convertirACursoDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por créditos: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 2: Buscar cursos por nivel de dificultad
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorNivelDificultadDTO(String nivel) {
        if (nivel == null || nivel.trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel no puede estar vacío");
        }
        
        try {
            List<Curso> cursos = cursoRepository.findAll();
            return cursos.stream()
                    .map(this::convertirACursoDTO)
                    .filter(cursoDTO -> cursoDTO.getNivelDificultad().equalsIgnoreCase(nivel.trim()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por nivel: " + e.getMessage());
        }
    }
    
    /**
     * FILTRO 3: Buscar cursos por carga académica
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarPorCargaAcademicaDTO(String carga) {
        if (carga == null || carga.trim().isEmpty()) {
            throw new IllegalArgumentException("La carga académica no puede estar vacía");
        }
        
        try {
            List<Curso> cursos = cursoRepository.findAll();
            return cursos.stream()
                    .map(this::convertirACursoDTO)
                    .filter(cursoDTO -> cursoDTO.getCargaAcademica().equalsIgnoreCase(carga.trim()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar cursos por carga académica: " + e.getMessage());
        }
    }
    
    // ============ MÉTODOS DE CONVERSIÓN PRIVADOS ============
    
    /**
     * Convierte una entidad Curso a CursoDTO
     */
    private CursoDTO convertirACursoDTO(Curso curso) {
        if (curso == null) {
            return null;
        }
        
        // Determinar nivel de dificultad basado en el código del curso
        String nivelDificultad = determinarNivelDificultad(curso.getCodigo());
        
        // Determinar carga académica basada en créditos y horas
        String cargaAcademica = determinarCargaAcademica(curso.getCreditos(), curso.getHorasSemanales());
        
        return new CursoDTO(
            curso.getId(),
            curso.getCodigo(),
            curso.getNombre(),
            curso.getDescripcion(),
            curso.getCreditos(),
            curso.getHorasSemanales(),
            nivelDificultad,
            cargaAcademica
        );
    }
    
    /**
     * Determina el nivel de dificultad basado en el código del curso
     */
    private String determinarNivelDificultad(String codigo) {
        if (codigo == null || codigo.length() < 4) {
            return "Básico";
        }
        
        // Extraer el número del código (ej: MAT101 -> 101)
        String numeroStr = codigo.substring(3);
        try {
            int numero = Integer.parseInt(numeroStr);
            if (numero >= 100 && numero <= 199) {
                return "Básico";
            } else if (numero >= 200 && numero <= 299) {
                return "Intermedio";
            } else if (numero >= 300) {
                return "Avanzado";
            } else {
                return "Básico";
            }
        } catch (NumberFormatException e) {
            return "Básico";
        }
    }
    
    /**
     * Determina la carga académica basada en créditos y horas semanales
     */
    private String determinarCargaAcademica(Integer creditos, Integer horasSemanales) {
        if (creditos == null) creditos = 0;
        if (horasSemanales == null) horasSemanales = 0;
        
        // Calcular un puntaje combinado
        int puntaje = creditos * 2 + horasSemanales;
        
        if (puntaje <= 8) {
            return "Baja";
        } else if (puntaje <= 16) {
            return "Media";
        } else {
            return "Alta";
        }
    }
    
    // ============ MÉTODOS ORIGINALES PARA COMPATIBILIDAD ============
    
    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        try {
            return cursoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los cursos: " + e.getMessage());
        }
    }
    
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
    
    @Transactional(readOnly = true)
    public List<Curso> obtenerOrdenadosPorCreditos() {
        try {
            return cursoRepository.findAllByOrderByCreditosAsc();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener cursos ordenados: " + e.getMessage());
        }
    }
    
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