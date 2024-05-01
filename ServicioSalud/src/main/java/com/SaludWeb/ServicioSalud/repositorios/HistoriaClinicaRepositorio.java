package com.SaludWeb.ServicioSalud.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SaludWeb.ServicioSalud.entidades.HistoriaClinica;



@Repository
public interface HistoriaClinicaRepositorio extends JpaRepository<HistoriaClinica,Long>{
    
   // Optional<HistoriaClinica> findByIdHistoriaClinica(Long id);

    //Optional findByDNIPacienteHC(Paciente paciente);
    
}
