package com.SaludWeb.ServicioSalud.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SaludWeb.ServicioSalud.entidades.Profesional;



@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional,Long>{
    
    Optional<Profesional> findByMatriculaProfesional(Long matriculaProfesional);
}

