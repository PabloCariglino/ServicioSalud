package com.SaludWeb.ServicioSalud.controladores;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.SaludWeb.ServicioSalud.entidades.Paciente;
import com.SaludWeb.ServicioSalud.entidades.Profesional;
import com.SaludWeb.ServicioSalud.entidades.Usuario;
import com.SaludWeb.ServicioSalud.servicios.PacienteServicio;
import com.SaludWeb.ServicioSalud.servicios.ProfesionalServicio;
import com.SaludWeb.ServicioSalud.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/paciente")
public class PacienteControlador {
    @Autowired
    private PacienteServicio pacienteServicio;

    @Autowired
    private ProfesionalServicio profesionalServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    // @Autowired
    // private TurnoServicio turnoServicio;

    // VISTA INICIAL DEL PACIENTE
    @GetMapping("/dashboard")
    public String vistaPaciente() {

        return "/paciente/vistaPaciente.html";
    }

    // ACTUALIZA EL USUARIO SETEANDOLE LOS DATOS DE EL PACIENTE
    @GetMapping("/registrar")
    public String registrarPaciente(HttpSession session, ModelMap modelo) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        // modelo.put("usuario", usuarioServicio.getOne(id));
        return "/paciente/altaDatos_paciente.html";
    }

    @PostMapping("/registro/{id}")
    public String registroPaciente(@RequestParam Long dniPaciente, @RequestParam String nombrePaciente,
            @RequestParam String ApellidoPaciente,
            @RequestParam("fechaNacimientoPaciente") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNacimientoPaciente,
            @RequestParam String obraSocial, @RequestParam Double telContacto, @RequestParam String intencionConsulta,
            ModelMap modelo, @PathVariable Long id) throws Exception {

        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Date fechaDate = dateFormat.parse(fechaNacimientoPaciente);
            pacienteServicio.crearPaciente(dniPaciente, nombrePaciente, ApellidoPaciente, fechaNacimientoPaciente,
                    obraSocial, telContacto, intencionConsulta);

            Paciente paciente = pacienteServicio.buscarPacientePorDNI(dniPaciente);
            usuarioServicio.actualizarUsuarioPacienteConDatos(paciente, id);

            // pacienteServicio.crearPaciente(dniPaciente, nombrePaciente, ApellidoPaciente,
            // fechaDate, obraSocial,
            // telContacto, intencionConsulta);
            modelo.put("exito", "Paciente registrado con éxito");
            return "/paciente/vistaPaciente.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "/paciente/altaDatos_paciente.html";

    }

    // MUESTRA LOS DATOS DE UN PACIENTE
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/datos")
    public String datosPaciente(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);

        return "/paciente/datos_paciente.html";
    }

    // ACTUALIZA LOS DATOS DE UN PACIENTE
    @PostMapping("/modificarDatos")
    public String modificarDatosPaciente() {

        return "/paciente/actualizar_datosPaciente.html";
    }

    // LISTADO DE PROFESIONALES PARA EL PACIENTE
    @GetMapping("/listadoProfesionales")
    public String listadoProfesionales(ModelMap modelo, HttpSession session) {

        List<Profesional> profesionales = profesionalServicio.listaProfesionales();
        modelo.addAttribute("profesionales", profesionales);
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "paciente/lista_profesionales.html";
    }

    @GetMapping("/seleccionar-doctor/{idPro}/{idPac}")
    public String seleccionarDoctor(@PathVariable String idPro, @PathVariable String idPac, ModelMap modelo) {
        pacienteServicio.seleccionarDoctor(Long.parseLong(idPac), Long.parseLong(idPro));
        // List<Profesional> profesionales = profesionalServicio.listaProfesionales();
        // modelo.addAttribute("profesionales", profesionales);
        // return "paciente/lista_profesionales.html";
        usuarioServicio.getOne(null);

        return "paciente/misTurnos.html";

    }
}
