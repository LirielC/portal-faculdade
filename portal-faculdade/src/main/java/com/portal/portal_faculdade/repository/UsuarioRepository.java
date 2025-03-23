package com.portal.portal_faculdade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.portal_faculdade.model.TipoUsuario;
import com.portal.portal_faculdade.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByMatricula(String matricula);
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    boolean existsByEmail(String email);
}