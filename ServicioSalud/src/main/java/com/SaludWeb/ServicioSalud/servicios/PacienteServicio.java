package com.SaludWeb.ServicioSalud.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SaludWeb.ServicioSalud.entidades.HistoriaClinica;
import com.SaludWeb.ServicioSalud.entidades.Paciente;
import com.SaludWeb.ServicioSalud.repositorios.PacienteRepositorio;

@Service
public class PacienteServicio {
    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @Autowired
    private ProfesionalServicio profesionalServicio;

    // CREAR DATOS DE UN PACIENTE
    public Paciente crearPaciente(Long dniPaciente, String nombrePaciente, String ApellidoPaciente,
            Date fechaNacimientoPaciente, String obraSocial, Double telContacto, String intencionConsulta) {

        Paciente paciente = new Paciente();
        paciente.setDniPaciente(dniPaciente);
        paciente.setNombrePaciente(nombrePaciente);
        paciente.setApellidoPaciente(ApellidoPaciente);
        paciente.setFechaNacimientoPaciente(fechaNacimientoPaciente);
        paciente.setObraSocial(obraSocial);
        paciente.setTelContacto(telContacto);
        paciente.setIntencionConsulta(intencionConsulta);

        return pacienteRepositorio.save(paciente);
    }

    // ACTUALIZAR DATOS DE UN PACIENTE
    public void actualizarDatosPaciente(Long id, Long dniPaciente, String nombrePaciente, String ApellidoPaciente,
            Date fechaNacimientoPaciente, String obraSocial, Double telContacto, String intencionConsulta) {

        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Paciente paciente = respuesta.get();

            paciente.setDniPaciente(dniPaciente);
            paciente.setNombrePaciente(nombrePaciente);
            paciente.setApellidoPaciente(ApellidoPaciente);
            paciente.setFechaNacimientoPaciente(fechaNacimientoPaciente);
            paciente.setObraSocial(obraSocial);
            paciente.setTelContacto(telContacto);
            paciente.setIntencionConsulta(intencionConsulta);

            pacienteRepositorio.save(paciente);

        }

    }

    // GUARDAR PACIENTE
    public void guardar(Paciente paciente) {

        pacienteRepositorio.save(paciente);
    }

    /*
     * // DAR DE BAJA PACIENTE/ELIMINAR Pacientes // SE ELIMINAN EN EL SERVICIO
     * USUARIO POR ROLES
     * public void bajaDePaciente(String id) {
     * Optional<Paciente> validacion = pacienteRepositorio.findById(id);
     * if (validacion.isPresent()) {
     * Paciente paciente = validacion.get();
     * 
     * pacienteRepositorio.save(paciente);
     * 
     * }
     * }
     */

    // LISTAR PACIENTES
    public List<Paciente> listaPacientes() {

        return (List<Paciente>) pacienteRepositorio.findAll();
    }

    // BUSCAR PACIENTE POR ID
    public Paciente buscarPacientePorID(Long id) {
        Optional<Paciente> respuesta = pacienteRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Paciente paciente = respuesta.get();
            return paciente;
        }
        return null;
    }

    // BUSCAR PACIENTE POR DNI
    public Paciente buscarPacientePorDNI(Long dniPaciente) {

        Optional<Paciente> respuesta = pacienteRepositorio.findBydniPaciente(dniPaciente);

        if (respuesta.isPresent()) {
            Paciente paciente = respuesta.get();
            return paciente;
        }

        return null;
    }

    // ACTUALIZAR PACIENTE CON SU HISTORIA CLINICA
    // le pasamos un paciente y le seteamos la lista de la historia
    // clinica al paciente
    public void actualizarDatosPacienteConHistoriaClinica(Long dniPaciente,
            List<HistoriaClinica> historiaClinicas) {

        Optional<Paciente> respuesta = pacienteRepositorio.findBydniPaciente(dniPaciente);

        if (respuesta.isPresent()) {

            Paciente paciente = respuesta.get();

            paciente.setHistoriaClinicas(historiaClinicas);

            pacienteRepositorio.save(paciente);

        }

    }

    public void seleccionarDoctor(Long idPac, Long idPro) {

        Paciente paciente = buscarPacientePorDNI(idPac);
        paciente.setProfesional(profesionalServicio.buscarProfesional(idPro));
        pacienteRepositorio.save(paciente);
    }
}
