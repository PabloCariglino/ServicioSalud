package com.SaludWeb.ServicioSalud.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SaludWeb.ServicioSalud.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

   Usuario findById = null;

   public Usuario findByEmail(String email);

   //buscar por email de usuario
   //@Query("SELECT u FROM Usuario u WHERE u.email = :email")
   //public Usuario buscarPorEmail(@Param("email") String email);

   //buscar por id de usuario
   //@Query("SELECT u FROM Usuario u WHERE u.email = :email")
   //public Usuario findById(@Param("idUsuario") String idUsuario);

}
