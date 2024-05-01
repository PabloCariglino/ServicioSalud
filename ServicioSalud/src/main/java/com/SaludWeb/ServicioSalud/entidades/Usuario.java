package com.SaludWeb.ServicioSalud.entidades;

import com.SaludWeb.ServicioSalud.enumeraciones.Rol;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombreUsuario;
    private String password;
    private String email;
    private Boolean estadoUsuario;

    @OneToOne
    private Profesional profesional;

    @OneToOne
    private Paciente paciente;

    @Enumerated()
    private Rol rol;

    @OneToOne
    private Imagen imagen;
}
