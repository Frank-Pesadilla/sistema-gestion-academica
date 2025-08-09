package com.gestionacademica.sistema_academico.repository;

import com.gestionacademica.sistema_academico.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    
    // Buscar estudiante por carnet
    Optional<Estudiante> findByCarnet(String carnet);
    
    // Buscar estudiante por email
    Optional<Estudiante> findByEmail(String email);
    
    // Verificar si existe un estudiante con el carnet dado
    boolean existsByCarnet(String carnet);
    
    // Verificar si existe un estudiante con el email dado
    boolean existsByEmail(String email);
    
    // Buscar estudiantes por nombre o apellido
    @Query("SELECT e FROM Estudiante e WHERE " +
           "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Estudiante> findByNombreOrApellidoContaining(@Param("termino") String termino);
    
    // Buscar estudiantes por rango de fechas de ingreso
    List<Estudiante> findByFechaIngresoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Buscar estudiantes que ingresaron en un año específico
    @Query("SELECT e FROM Estudiante e WHERE YEAR(e.fechaIngreso) = :año")
    List<Estudiante> findByAñoIngreso(@Param("año") int año);
    
    // Buscar estudiantes por carnet o email
    @Query("SELECT e FROM Estudiante e WHERE e.carnet = :carnet OR e.email = :email")
    Optional<Estudiante> findByCarnetOrEmail(@Param("carnet") String carnet, @Param("email") String email);
    
    // Obtener estudiantes ordenados por fecha de ingreso
    List<Estudiante> findAllByOrderByFechaIngresoAsc();
    
    List<Estudiante> findAllByOrderByFechaIngresoDesc();
}