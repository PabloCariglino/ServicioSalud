package com.SaludWeb.ServicioSalud.entidades;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class HistoriaClinica {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   
    private String especialidad;
    private Long matriculaAtencionProfesional; //la matricula del profesional que lo atendio
    private String historialMedico;
    private Date fechaDeAtencion;
    private String prepaga; //Obra Social
    
    @OneToOne
    private Paciente paciente;
    @ManyToOne
    private Profesional profesinal;
}
