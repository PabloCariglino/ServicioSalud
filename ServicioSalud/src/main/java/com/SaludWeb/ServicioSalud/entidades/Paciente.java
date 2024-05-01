package com.SaludWeb.ServicioSalud.entidades;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Paciente {

    
    //@GeneratedValue(generator = "uuid")
    //@GenericGenerator(name = "uuid", strategy = "uuid2") // Estrategia alternativa
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dniPaciente;
    private String nombrePaciente;
    private String ApellidoPaciente;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoPaciente;
    private String obraSocial;
    private Double telContacto;
    private String intencionConsulta;

    @OneToMany
    private List<HistoriaClinica> historiaClinicas;

    @OneToMany
    private List<Turno> turnos;

    // (mappedBy = "paciente", fetch = FetchType.LAZY)
    // @OneToMany
    // private List<Profesional> profesional;
    @OneToOne
    private Profesional profesional;
}
