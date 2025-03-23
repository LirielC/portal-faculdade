package com.portal.portal_faculdade.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.portal.portal_faculdade.model.TipoUsuario;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Tentando autenticar usuário com matrícula: {}", username);
        
        Usuario usuario = usuarioRepository.findByMatricula(username)
            .orElseThrow(() -> {
                logger.error("Usuário não encontrado com a matrícula: {}", username);
                return new UsernameNotFoundException("Usuário não encontrado com a matrícula: " + username);
            });
            
        logger.info("Usuário encontrado: {}, Senha hash: {}, Authorities: {}, Ativo: {}", 
            usuario.getNome(), 
            usuario.getSenha(),
            usuario.getAuthorities(),
            usuario.isAtivo());
            
        return usuario;
    }

    public Usuario salvar(Usuario usuario) {
        logger.info("Salvando novo usuário: {}", usuario.getNome());
        
        // Verifica se o email já existe
        if (emailExiste(usuario.getEmail())) {
            logger.error("Email já existe: {}", usuario.getEmail());
            throw new RuntimeException("Email já está em uso");
        }
        
        // Codifica a senha apenas se ela não estiver já codificada
        if (!usuario.getSenha().startsWith("$2a$")) {
            String senhaEncriptada = passwordEncoder.encode(usuario.getSenha());
            logger.info("Senha original: {}, Senha codificada: {}", usuario.getSenha(), senhaEncriptada);
            usuario.setSenha(senhaEncriptada);
        }
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        logger.info("Usuário salvo com sucesso: {}, ID: {}, Senha hash: {}", 
            usuarioSalvo.getNome(), 
            usuarioSalvo.getId(),
            usuarioSalvo.getSenha());
        return usuarioSalvo;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> buscarPorTipo(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void redefinirSenha(String matricula, String novaSenha) {
        logger.info("Redefinindo senha para usuário com matrícula: {}", matricula);
        
        Usuario usuario = usuarioRepository.findByMatricula(matricula)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com a matrícula: " + matricula));
            
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        
        logger.info("Senha redefinida com sucesso para usuário: {}", usuario.getNome());
    }
}