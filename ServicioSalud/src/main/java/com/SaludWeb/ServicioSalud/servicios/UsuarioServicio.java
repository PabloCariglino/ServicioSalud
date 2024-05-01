package com.SaludWeb.ServicioSalud.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.SaludWeb.ServicioSalud.entidades.Imagen;
import com.SaludWeb.ServicioSalud.entidades.Paciente;
import com.SaludWeb.ServicioSalud.entidades.Profesional;
import com.SaludWeb.ServicioSalud.entidades.Usuario;
import com.SaludWeb.ServicioSalud.enumeraciones.Rol;
import com.SaludWeb.ServicioSalud.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    // CREAR USUARIO
    @Transactional
    public void registrarUsuario(String nombreUsuario, String email, Boolean estadoUsuario, String password,
            String password2,
            MultipartFile archivo)
            throws Exception {

        validar(nombreUsuario, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombreUsuario(nombreUsuario);
        usuario.setEmail(email);
        usuario.setEstadoUsuario(true);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        // Obtener información de autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (email.equals("admin@admin.com")) {
            usuario.setRol(Rol.ADMIN);
        } else if (email.endsWith("@profesional.com")) {
            // Verificar si el usuario actual tiene el rol de ADMIN
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
                // El usuario actual tiene el rol ADMIN
                usuario.setRol(Rol.PROFESIONAL);
            } else {
                throw new IllegalStateException("Solo el rol ADMIN puede registrar profesionales");
            }
        } else {
            usuario.setRol(Rol.USER);
        }

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);
    }

    // ACTUALIZAR EL PERFIL DEL USUARIO
    @Transactional
    public void actualizarUsuario(MultipartFile archivo, Long idUsuario, String nombreUsuario, String email,
            Boolean estadoUsuario,
            String password,
            String password2) throws Exception {

        validar(nombreUsuario, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setEmail(email);
            usuario.setEstadoUsuario(true);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            // usuario.setRol(Rol.USER);

            Long idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }

    }

     // DEVUELVE UN USUARIO POT ID
     public Usuario getOne(Long id) {
        return usuarioRepositorio.getOne(id);
    }

    // LISTAR TODOS los Usuarios
    public List<Usuario> ListaTotalUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;

    }

    // LISTAR Usuarios paciente
    public List<Usuario> ListaUsuariosPacientes() {

        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    // DAR DE BAJA USUARIOS/ELIMINAR Usuarios
    public void bajaDeUsuario(Long id) {
        Optional<Usuario> validacion = usuarioRepositorio.findById(id);
        if (validacion.isPresent()) {
            Usuario usuario = validacion.get();
            usuario.setEstadoUsuario(false);
            usuarioRepositorio.save(usuario);

        }
    }

    // VALIDACION PARA REGISTRO DE USUARIO
    private void validar(String nombre, String email, String password, String password2) throws Exception {

        if (nombre.isEmpty() || nombre == null) {
            throw new Exception("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new Exception("el email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new Exception("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new Exception("Las contraseñas ingresadas deben ser iguales");
        }

    }

    // El servicio debe implementar ↓
    // implements UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(email); // ←MODIFICAR
        if (usuario != null) { // ←MODIFICAR

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority perm = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(perm);

            // Luego de validar el usuario guardamos una sesion web
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            // La session contiene los datos del usuario recuperado de la base de datos
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos); // ←MODIFICAR
        } else {
            return null;
        }
    }

    public Usuario getUsuarioConImagen(Long id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    // DEVUELVEN LISTA DE USUARIOS USER Y USUARIOS PROFESIONAL
    public List<Usuario> listaUsuariosConRolUser() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return filtrarUsuariosPorRol(usuarios, Rol.USER);
    }

    public List<Usuario> listaUsuariosConRolProfesional() {
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return filtrarUsuariosPorRol(usuarios, Rol.PROFESIONAL);
    }

    private List<Usuario> filtrarUsuariosPorRol(List<Usuario> usuarios, Rol rol) {
        return usuarios.stream()
                .filter(usuario -> tieneRol(usuario, rol))
                .collect(Collectors.toList());
    }

    private boolean tieneRol(Usuario usuario, Rol rol) {
        return usuario.getRol() == rol;
    }

    // ACTUALIZAR EL PERFIL DEL USUARIO PROFESIONAL CON LOS DATOS
    @Transactional
    public void actualizarUsuarioProfesionalConDatos(Profesional profesional, Long idUsuario) throws Exception {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setProfesional(profesional);

            usuarioRepositorio.save(usuario);
        }

    }

    // ACTUALIZAR EL PERFIL DEL USUARIO PACIENTE CON LOS DATOS
    @Transactional
    public void actualizarUsuarioPacienteConDatos(Paciente paciente, Long idUsuario) throws Exception {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setPaciente(paciente);

            usuarioRepositorio.save(usuario);
        }

    }

//OBTENER USUARIO ACTUAL AUTENTICADO en el inicio de session actual
    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        return null; // o lanza una excepción si prefieres manejar el caso en que no hay usuario autenticado
    }

}
