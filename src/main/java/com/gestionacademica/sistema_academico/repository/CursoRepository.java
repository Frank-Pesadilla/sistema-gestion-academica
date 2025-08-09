package com.gestionacademica.sistema_academico.repository;

import com.gestionacademica.sistema_academico.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    // Buscar curso por código
    Optional<Curso> findByCodigo(String codigo);
    
    // Verificar si existe un curso con el código dado
    boolean existsByCodigo(String codigo);
    
    // Buscar cursos por nombre (búsqueda parcial e insensible a mayúsculas)
    List<Curso> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar cursos por rango de créditos
    List<Curso> findByCreditosBetween(Integer creditosMin, Integer creditosMax);
    
    // Buscar cursos por número exacto de créditos
    List<Curso> findByCreditos(Integer creditos);
    
    // Buscar cursos con determinadas horas semanales
    List<Curso> findByHorasSemanales(Integer horasSemanales);
    
    // Buscar cursos por código o nombre
    @Query("SELECT c FROM Curso c WHERE " +
           "LOWER(c.codigo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Curso> findByCódigoOrNombreContaining(@Param("termino") String termino);
    
    // Obtener cursos ordenados por créditos
    List<Curso> findAllByOrderByCreditosAsc();
    
    List<Curso> findAllByOrderByCreditosDesc();
}