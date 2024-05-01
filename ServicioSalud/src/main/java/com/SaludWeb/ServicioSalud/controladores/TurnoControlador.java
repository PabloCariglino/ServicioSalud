package com.SaludWeb.ServicioSalud.controladores;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.SaludWeb.ServicioSalud.servicios.TurnoServicio;

@Controller
@RequestMapping("/turnos")
public class TurnoControlador {

    @Autowired
    TurnoServicio turnoServicio;
    

    @GetMapping("/solicitudTurno")
    public String mostrarFormularioSolicitudTurno(ModelMap modelo) {
       //lógica para obtener las especialidades disponibles
        //modelo.put("especialidades", obtenerEspecialidadesDisponibles());
        return "paciente/agendar_turno.html";
    }

    @PostMapping("/emisionTurno")
    public String procesarSolicitudTurno(@RequestParam String especialidad,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha,
                                          @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime hora,
                                          ModelMap modelo, Authentication authentication) {
        try {
           // Paciente paciente = obtenerPacienteAutenticado(authentication);
            //Turno turno = turnoServicio.solicitarTurno(paciente, fecha, hora, especialidad);
            //modelo.put("exito", "Turno solicitado con éxito. Estado del turno: " + turno.getEstadoTurno());
        } catch (Exception e) {
            modelo.put("error", "Error al solicitar el turno: " + e.getMessage());
        }
        return "redirect:/paciente/vistaPaciente.html";
    }
}
