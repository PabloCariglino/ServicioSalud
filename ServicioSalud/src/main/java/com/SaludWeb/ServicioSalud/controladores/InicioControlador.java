package com.SaludWeb.ServicioSalud.controladores;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.SaludWeb.ServicioSalud.entidades.Usuario;
import com.SaludWeb.ServicioSalud.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class InicioControlador {

    // VISTA INICIAL DE LA PAGINA
    @GetMapping("/index")
    public String inicio() {

        return "index.html";
    }

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Registro de usuario
    @GetMapping("/registrar")
    public String registrarUsuario() {

        return "registro_usuario.html";
    }

    @PostMapping("/registroUsuario")
    public String registro(@RequestParam String nombreUsuario, @RequestParam String email, Boolean estadoUsuario,
            @RequestParam String password,
            String password2, ModelMap modelo, @RequestPart MultipartFile archivo) {

        try {

            usuarioServicio.registrarUsuario(nombreUsuario, email, estadoUsuario, password, password2, archivo);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (Exception ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombreUsuario);
            modelo.put("email", email);

            return "registro_usuario.html";
        }

    }

    // LOGUEO DE USUARIO
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválidos");
            return "login.html";
        } else {
            modelo.put("exito", "Bienvenido");
            // Obtener el rol del usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PROFESIONAL"))) {
                return "/profesional/vistaProfesional.html";
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                return "/paciente/vistaPaciente.html";
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                return "/admin/vistaAdmin.html";
            } else {
                // Si no se encuentra un rol válido, puedes redirigir a una página de error o
                // manejarlo de alguna otra manera
                return "/login.html";
            }
        }
    }

    // ACTUALIZAR USUARIO
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN' , 'ROLE_PROFESIONAL')")
    @GetMapping("/actualizarPerfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "actualizar_usuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN' , 'ROLE_PROFESIONAL')")
    @PostMapping("/actualizarPerfil/{idUsuario}")
    public String actualizarUsuario(
            @RequestPart MultipartFile archivo,
            @PathVariable Long idUsuario,
            @RequestParam String nombreUsuario,
            @RequestParam String email,
            Boolean estadoUsuario,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo) {

        try {
            // Obtener el usuario antes de actualizar
            Usuario usuario = usuarioServicio.getOne(idUsuario);

            // Verificar si el usuario existe
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }

            // Actualizar el usuario
            usuarioServicio.actualizarUsuario(archivo, idUsuario, nombreUsuario, email, estadoUsuario, password,
                    password2);

            modelo.put("exito", "Usuario actualizado correctamente!");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PROFESIONAL"))) {
                return "/profesional/vistaProfesional.html";
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                return "/paciente/vistaPaciente.html";
            } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                return "/admin/vistaAdmin.html";
            } else {
                // Si no se encuentra un rol válido, puedes redirigir a una página de error o
                // manejarlo de alguna otra manera
                return "/actualizar_usuario.html";
            }
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombreUsuario);
            modelo.put("email", email);

            return "actualizar_usuario.html";
        }
    }

}