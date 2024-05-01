package com.SaludWeb.ServicioSalud.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SaludWeb.ServicioSalud.entidades.HistoriaClinica;
import com.SaludWeb.ServicioSalud.entidades.Paciente;
import com.SaludWeb.ServicioSalud.entidades.Profesional;
import com.SaludWeb.ServicioSalud.enumeraciones.Horario;
import com.SaludWeb.ServicioSalud.repositorios.HistoriaClinicaRepositorio;
import com.SaludWeb.ServicioSalud.repositorios.ProfesionalRepositorio;

@Service
public class ProfesionalServicio {

    @Autowired
    private ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    private HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    // CREAR PROFESIONAL datos
    public Profesional crearProfesional(Long matriculaProfesional, String nombreProfesional, String apellidoProfesional,
            int edadProfesional,
            String especialidadProfesional, Long puntuacionProfesional,
            double precioConsulta, String caracteristicaDeOferta, String horario) {

        Profesional profesional = new Profesional();
        profesional.setMatriculaProfesional(matriculaProfesional);
        profesional.setNombreProfesional(nombreProfesional);
        profesional.setApellidoProfesional(apellidoProfesional);
        profesional.setEdadProfesional(edadProfesional);
        profesional.setEspecialidadProfesional(especialidadProfesional);
        profesional.setPuntuacionProfesional(puntuacionProfesional);
        // profesional.setHorariosDisponibles(horariosDisponibles); // Agregar ARRIBA!
        profesional.setPrecioConsulta(precioConsulta);
        profesional.setCaracteristicaDeOferta(caracteristicaDeOferta);// (telemedicina, presencial, ubicación, obras
                                                                      // sociales, datos de contacto).
        profesional.setHorario(Horario.valueOf(horario));
        return profesionalRepositorio.save(profesional);
    }

    // ACTUALIZAR DATOS DE UN PROFESIONAL
    public void actualizarDatosProfesional(Long id, Long matriculaProfesional, String nombreProfesional,
            String apellidoProfesional,
            int edadProfesional,
            String especialidadProfesional, Long puntuacionProfesional,
            double precioConsulta) {

        Optional<Profesional> respuesta = profesionalRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Profesional profesional = new Profesional();
            profesional.setMatriculaProfesional(matriculaProfesional);
            profesional.setNombreProfesional(nombreProfesional);
            profesional.setApellidoProfesional(apellidoProfesional);
            profesional.setEdadProfesional(edadProfesional);
            profesional.setEspecialidadProfesional(especialidadProfesional);
            profesional.setPuntuacionProfesional(puntuacionProfesional);
            // profesional.setHorariosDisponibles(horariosDisponibles); // Agregar ARRIBA!
            profesional.setPrecioConsulta(precioConsulta);
            profesionalRepositorio.save(profesional);

        }

    }

    // LISTAR PROFESIONALES
    public List<Profesional> listaProfesionales() {

        return (List<Profesional>) profesionalRepositorio.findAll();
    }

    // GUARDAR PROFESIONALES
    public void guardar(Profesional profesional) {

        profesionalRepositorio.save(profesional);
    }

    /*
     * // DAR DE BAJA PROFESIONAL /ELIMINAR PROFESIONALES // SE ELIMINAN EN EL
     * SERVICIO DE USUARIO POR LOS ROLES
     * public void bajaDeProfesional(Long id) {
     * Optional<Profesional> validacion = profesionalRepositorio.findById(id);
     * if (validacion.isPresent()) {
     * Profesional profesional = validacion.get();
     * profesional.setEstadoProfesional(false);
     * profesionalRepositorio.save(profesional);
     * }
     * }
     */

    // BUSCAR PROFESIONAL POR ID
    public Profesional buscarProfesional(Long matricula) {

        Optional<Profesional> respuesta = profesionalRepositorio.findByMatriculaProfesional(matricula);

        Profesional profesional = respuesta.get();
        return profesional;
    }

    // CREAR HISTORIA CLINICA DE UN PACIENTE, SETEA LA HISTORIA CLINICA AL USUARIO
    // PACIENTE/user
    public HistoriaClinica crearHistoriaClinicaPaciente(Paciente paciente, String especialidad,
            Long matriculaAtencionProfesional,
            String historialMedico,
            Date fechaDeAtencion,
            String prepaga) {

        // Obtener el usuario profesional actualmente autenticado
       // Usuario usuarioProfesional = usuarioServicio.obtenerUsuarioAutenticado();
       // Profesional profesional = usuarioProfesional.getProfesional();

        // Validar si el usuario autenticado tiene el rol de Profesional
       // if (profesional == null) {
       //     throw new IllegalStateException("El usuario autenticado no es un profesional.");
       // }

        HistoriaClinica historiaClinica = new HistoriaClinica();
        // paciente.setHistoriaClinicas(null);

        historiaClinica.setPaciente(paciente);

        historiaClinica.setEspecialidad(especialidad);
        historiaClinica.setMatriculaAtencionProfesional(matriculaAtencionProfesional);
        historiaClinica.setHistorialMedico(historialMedico);
        historiaClinica.setFechaDeAtencion(fechaDeAtencion);
        historiaClinica.setPrepaga(prepaga);

        return historiaClinicaRepositorio.save(historiaClinica);
    }

    // ACTUALIZAR HISTORIA CLINICA DE UN PACIENTE
    public void actualizarHistoriaClinicaPaciente(Long id, String especialidad, Long matriculaAtencionProfesional,
            String historialMedico,
            Date fechaDeAtencion,
            String prepaga) {

        Optional<HistoriaClinica> respuesta = historiaClinicaRepositorio.findById(id);

        if (respuesta.isPresent()) {

            HistoriaClinica historiaClinica = new HistoriaClinica();

            historiaClinica.setEspecialidad(especialidad);
            historiaClinica.setMatriculaAtencionProfesional(matriculaAtencionProfesional);
            historiaClinica.setHistorialMedico(historialMedico);
            historiaClinica.setFechaDeAtencion(fechaDeAtencion);
            historiaClinica.setPrepaga(prepaga);

            historiaClinicaRepositorio.save(historiaClinica);

        }

    }

}
