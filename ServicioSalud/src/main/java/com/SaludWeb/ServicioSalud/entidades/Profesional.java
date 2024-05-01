package com.SaludWeb.ServicioSalud.entidades;

import java.util.List;

import com.SaludWeb.ServicioSalud.enumeraciones.Horario;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = false)
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matriculaProfesional;
    private String nombreProfesional;
    private String apellidoProfesional;
    private int edadProfesional;
    private String especialidadProfesional;
    private Long puntuacionProfesional;
    // private LocalTime horariosDisponibles;
    private double precioConsulta;
    private String caracteristicaDeOferta; // (telemedicina, presencial, ubicación, obras sociales, datos de contacto).

    @OneToMany
    private List<Turno> turnos;

    @Enumerated(EnumType.STRING)
    private Horario horario;

    @OneToMany
    private List<HistoriaClinica> historiaClinicas;

    @ManyToOne
    private Paciente paciente;

}
