package com.gestionacademica.sistema_academico.repository;

import com.gestionacademica.sistema_academico.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    
    // Buscar profesor por email
    Optional<Profesor> findByEmail(String email);
    
    // Verificar si existe un profesor con el email dado
    boolean existsByEmail(String email);
    
    // Buscar profesores por especialidad
    List<Profesor> findByEspecialidadContainingIgnoreCase(String especialidad);
    
    // Buscar profesores por nombre o apellido
    @Query("SELECT p FROM Profesor p WHERE " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Profesor> findByNombreOrApellidoContaining(@Param("termino") String termino);
    
    // Contar profesores por especialidad
    long countByEspecialidad(String especialidad);
}