package com.SaludWeb.ServicioSalud.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.SaludWeb.ServicioSalud.entidades.Profesional;
import com.SaludWeb.ServicioSalud.entidades.Usuario;
import com.SaludWeb.ServicioSalud.servicios.PacienteServicio;
import com.SaludWeb.ServicioSalud.servicios.ProfesionalServicio;
import com.SaludWeb.ServicioSalud.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;
    @Autowired
    private PacienteServicio pacienteServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String inicioAdmin() {

        return "/admin/vistaAdmin.html";
    }

    // REGISTRA DATOS DE UN PROFESIONAL
    @GetMapping("/registrarDatosProfesional/{id}")
    public String registrarProfesional(@PathVariable Long id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id)); // inyectamos mediante la llave usuario el usuario a modificar (especialidad)
        return "admin/altaDatos_profesional.html";
    }

    @PostMapping("/registroDatosProfesional/{id}")
    public String registroProfesional(@RequestParam Long matriculaProfesional, @RequestParam String nombreProfesional,
            @RequestParam String apellidoProfesional, @RequestParam int edadProfesional,
            @RequestParam String especialidadProfesional, @RequestParam Long puntuacionProfesional,
            @RequestParam double precioConsulta, @RequestParam String caracteristicaDeOferta,@RequestParam String horario, ModelMap modelo,
            @PathVariable Long id)
            throws Exception {

        try {
            profesionalServicio.crearProfesional(matriculaProfesional, nombreProfesional, apellidoProfesional,
                    edadProfesional, especialidadProfesional, puntuacionProfesional, precioConsulta,
                    caracteristicaDeOferta,horario);

            Profesional profesional = profesionalServicio.buscarProfesional(matriculaProfesional);
            usuarioServicio.actualizarUsuarioProfesionalConDatos(profesional, id);

            modelo.put("exito", "Profesional registrado con éxito");

            return "/admin/vistaAdmin.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "admin/altaDatos_profesional.html";

    }

    // Registro de usuario profesional
    @GetMapping("/registrarUsuarioProfesional")
    public String registrarUsuario() {

        return "admin/altaUsuario_profesional.html";
    }

    @PostMapping("/registroUsuarioProfesional")
    public String registroUsuarioProfesional(@RequestParam String nombreUsuario, @RequestParam String email,
            Boolean estadoUsuario,
            @RequestParam String password,
            String password2, ModelMap modelo, @RequestPart MultipartFile archivo) {

        try {

            usuarioServicio.registrarUsuario(nombreUsuario, email, estadoUsuario, password, password2, archivo);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "/admin/vistaAdmin.html";
        } catch (Exception ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombreUsuario);
            modelo.put("email", email);

            return "admin/altaUsuario_profesional.html";
        }

    }

    // LOGUEO DE USUARIO profesional
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválidos");
            return "login.html";
        } else {
            modelo.put("exito", "Bienvenido");
            return "/admin/vistaAdmin.html";
        }
    }

    // MUESTRA LISTA DE USUARIOS CON EL ROL DE PROFESIONALES DEL ADMIN
    @GetMapping("/listadoProfesionalesAdmin")
    public String listadoProfesionalesAdmin(ModelMap modelo) {

        List<Usuario> usuariosProfesionales = usuarioServicio.listaUsuariosConRolProfesional();
        modelo.addAttribute("usuariosProfesionales", usuariosProfesionales);

        return "admin/listaProfesionales_admin";
    }

    // MUESTRA LISTA DE USUARIOS CON EL ROL DE USER/PACIENTES DEL ADMIN
    @GetMapping("/listadoPacientesAdmin")
    public String listadoPacientesAdmin(ModelMap modelo) {

        List<Usuario> usuariosPacientes = usuarioServicio.listaUsuariosConRolUser();
        modelo.addAttribute("usuariosPacientes", usuariosPacientes);

        return "admin/listaPacientes_admin";
    }

}
