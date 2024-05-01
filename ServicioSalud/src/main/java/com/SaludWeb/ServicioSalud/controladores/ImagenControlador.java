package com.SaludWeb.ServicioSalud.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.SaludWeb.ServicioSalud.entidades.Usuario;
import com.SaludWeb.ServicioSalud.servicios.UsuarioServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    private final UsuarioServicio usuarioServicio;

    public ImagenControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioServicio.getUsuarioConImagen(id);

            if (usuario != null && usuario.getImagen() != null) {
                byte[] imagenBytes = usuario.getImagen().getContenido();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentLength(imagenBytes.length);

                return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Loguea el error
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


