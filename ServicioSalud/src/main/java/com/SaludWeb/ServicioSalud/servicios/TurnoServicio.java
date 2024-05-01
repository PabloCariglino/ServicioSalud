package com.SaludWeb.ServicioSalud.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SaludWeb.ServicioSalud.entidades.Paciente;
import com.SaludWeb.ServicioSalud.entidades.Turno;
import com.SaludWeb.ServicioSalud.repositorios.TurnoRepositorio;

@Service
public class TurnoServicio {

    @Autowired
    private TurnoRepositorio turnoRepositorio;

    @Autowired
    PacienteServicio pacienteServicio;

    @Autowired
    ProfesionalServicio profesionalServicio;

    public Turno solicitarTurno(Paciente paciente, LocalDate fecha, LocalTime hora, String especialidad) {
        // Lógica para crear y guardar el turno en la base de datos
        // estado del turno (pendiente, confirmado, etc.)
        Turno turno = new Turno();
        turno.setPaciente(paciente);
        turno.setFecha(fecha);
        turno.setHora(hora);
        turno.setEspecialidad(especialidad);
        turno.setEstadoTurno(false); // Estado inicial Guardar el turno en la base de datos
        return turnoRepositorio.save(turno);
    }






    
    // Obtener todos los turnos
    public List<Turno> obtenerTodosLosTurnos() {
        return turnoRepositorio.findAll();
    }

    // Obtener los turnos de un paciente específico
    public List<Turno> obtenerTurnosPorPaciente(Long pacienteId) {
        return turnoRepositorio.findByPacienteId(pacienteId);
    }

    // Obtener los turnos de un profesional específico
    public List<Turno> obtenerTurnosPorProfesional(Long profesionalId) {
        return turnoRepositorio.findByProfesionalId(profesionalId);
    }

    // Cancelar un turno
    public void cancelarTurno(Long turnoId) {
        // Puedes agregar lógica adicional, por ejemplo, verificar si el turno existe
        // antes de cancelarlo
        turnoRepositorio.deleteById(turnoId);
    }

    // Guardar un nuevo turno
    public void guardarTurno(Turno turno) {
        // Puedes agregar lógica de validación u otras operaciones antes de guardar el turno
        turnoRepositorio.save(turno);
    }

}
