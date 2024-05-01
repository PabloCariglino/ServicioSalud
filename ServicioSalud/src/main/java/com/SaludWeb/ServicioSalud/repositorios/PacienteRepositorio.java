package com.SaludWeb.ServicioSalud.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SaludWeb.ServicioSalud.entidades.Paciente;

@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente,Long>{
    
    //QUERY QUE BUSCA A PACIENTE POR ID DNI/ CREAR SU RESPECTIVO METODO EN SERVICIO
    //@Query("SELECT p FROM Paciente p WHERE p.dniPaciente = :dniPaciente")
    //Optional<Paciente> buscarPorDNIPaciente(@Param("dniPacientel") Long dniPaciente);

    Optional<Paciente> findBydniPaciente(Long dniPaciente);

}
