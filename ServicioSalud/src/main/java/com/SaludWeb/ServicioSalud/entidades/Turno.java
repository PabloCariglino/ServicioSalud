package com.SaludWeb.ServicioSalud.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Turno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;
    private Boolean estadoTurno;
    private String especialidad;

    @ManyToOne
    private Paciente paciente;  // Relación con Paciente

    @ManyToOne
    private Profesional profesional;  // Relación con Profesional.
}
