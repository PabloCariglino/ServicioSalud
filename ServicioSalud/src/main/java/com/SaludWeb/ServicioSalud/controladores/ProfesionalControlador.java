package com.SaludWeb.ServicioSalud.controladores;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.SaludWeb.ServicioSalud.entidades.HistoriaClinica;
import com.SaludWeb.ServicioSalud.entidades.Paciente;
import com.SaludWeb.ServicioSalud.entidades.Usuario;
import com.SaludWeb.ServicioSalud.servicios.HistoriaClinicaServicio;
import com.SaludWeb.ServicioSalud.servicios.PacienteServicio;
import com.SaludWeb.ServicioSalud.servicios.ProfesionalServicio;
import com.SaludWeb.ServicioSalud.servicios.UsuarioServicio;

@Controller
@RequestMapping("/profesional")
public class ProfesionalControlador {
    @Autowired
    private ProfesionalServicio profesionalServicio;

    @Autowired
    private PacienteServicio pacienteServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    // @Autowired
    // private HistoriaClinicaRepositorio historiaClinicaRepositorio;

    @Autowired
    private HistoriaClinicaServicio historiaClinicaServicio;

    // VISTA INICIO DEL PROFESIONAL
    @GetMapping("/dashboard")
    public String vistaProfesional() {

        return "profesional/vistaProfesional.html";
    }

    // LISTADO DE TURNOS QUE POSEE EL PROFESIONAL
    @GetMapping("/listadoDeTurnos")
    public String listadoDeTurnos() {

        return "profesional/lista_turnosProfesional.html";
    }

    // LISTADO DE PACIENTES QUE POSEE EL PROFESIONAL
    @GetMapping("/listadoPacientes")
    public String listadoPacientes(ModelMap modelo) {
        List<Usuario> usuariosPacientes = usuarioServicio.listaUsuariosConRolUser();

        modelo.addAttribute("usuariosPacientes", usuariosPacientes);

        return "profesional/lista_pacientes";
    }

    // DATOS DEL PROFESIONAL
    @GetMapping("/datos")
    public String datosProfesional() {

        return "/profesional/datos_profesional.html";
    }

    // ACTUALIZAR LOS DATOS DEL PROFESIONAL
    @GetMapping("/modificarDatos")
    public String modificarDatosProfesional() {

        return "/profesional/actualizar_datosProfesional.html";
    }

    // VISTA HISTORIA CLINICA DEL PACIENTE
    @GetMapping("/historiaClinica/{id}")
    public String historiaClinicaPaciente(@PathVariable Long id, ModelMap modelo) {

        try {
            
            List<HistoriaClinica> listaHC = historiaClinicaServicio.listarHistoriasClinicas();
            modelo.put("listaHC", listaHC);
        } catch (Exception e) {
            
        }

        return "/profesional/vista_historia_clinica.html";
    }

    // ACTUALIZAR LA HISTORIA CLINICA DEL PACIENTE
    @GetMapping("/modificarHistoriaClinica")
    public String modificarHistoriaClinicaPaciente() {

        return "/profesional/actualizar_historiaclinica.html";
    }

    // REGISTRA HISTORIA CLINICA DE UN PACIENTE
    @GetMapping("/registrarHistoriaClinicaPaciente/{id}")
    public String registrarHistoriaClinicaPaciente(@PathVariable Long id, ModelMap modelo) {
        
        modelo.put("usuario", usuarioServicio.getOne(id)); // inyectamos mediante la llave usuario el usuario a modificar (especialidad)
                                                           
        return "profesional/alta_historia_clinica.html";
    }

    @PostMapping("/registroHistoriaClinicaPaciente/{id}")
    public String registroHistoriaClinicaPaciente(@RequestParam String especialidad,
            @RequestParam Long matriculaAtencionProfesional,
            @RequestParam String historialMedico,
            @RequestParam("fechaDeAtencion") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDeAtencion,
            @RequestParam String prepaga, ModelMap modelo,
            @PathVariable Long id)
            throws Exception {

        try {

            Usuario usuario = usuarioServicio.getOne(id);

            Paciente paciente = pacienteServicio.buscarPacientePorDNI(usuario.getPaciente().getDniPaciente());

            HistoriaClinica hc = profesionalServicio.crearHistoriaClinicaPaciente(paciente, especialidad,
                    matriculaAtencionProfesional,
                    historialMedico, fechaDeAtencion, prepaga);

            // HistoriaClinica historiaClinica =
            // historiaClinicaServicio.buscarHistoriaClinicaPorID(id);

            List<HistoriaClinica> listaHistoriaClinicas = paciente.getHistoriaClinicas();

            listaHistoriaClinicas.add(hc);

            pacienteServicio.actualizarDatosPacienteConHistoriaClinica( paciente.getDniPaciente(), listaHistoriaClinicas);
            //paciente.setHistoriaClinicas(listaHistoriaClinicas);

            // pacienteServicio.actualizarDatosPacienteConHistoriaClinica(paciente,
            // paciente.getDniPaciente());

            modelo.put("exito", "Historia clinica registrada con éxito");

            return "profesional/vistaProfesional.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "/profesional/alta_historia_clinica.html";

    }
}
