package com.portal.portal_faculdade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.portal.portal_faculdade.form.RegistroForm;
import com.portal.portal_faculdade.model.TipoUsuario;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.service.UsuarioService;
import com.portal.portal_faculdade.service.VerificacaoMatriculaService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class RegistroController {
    
    private final UsuarioService usuarioService;
    private final VerificacaoMatriculaService verificacaoService;
    
    @Autowired
    public RegistroController(UsuarioService usuarioService, VerificacaoMatriculaService verificacaoService) {
        this.usuarioService = usuarioService;
        this.verificacaoService = verificacaoService;
    }
    
    @GetMapping("/registro")
    public String mostrarFormulario(HttpSession session, Model model) {
        // Verifica se a matrícula foi validada
        String matricula = (String) session.getAttribute("matriculaValidada");
        if (matricula == null) {
            return "redirect:/verificar-matricula";
        }
        
        // Preenche informações pré-cadastradas
        RegistroForm form = new RegistroForm();
        form.setMatricula(matricula);
        form.setNome((String) session.getAttribute("nomePreCadastrado"));
        form.setEmail((String) session.getAttribute("emailPreCadastrado"));
        
        model.addAttribute("registroForm", form);
        return "registro";
    }
    
    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form, 
                          BindingResult result,
                          HttpSession session,
                          Model model) {
        String matricula = (String) session.getAttribute("matriculaValidada");
        if (matricula == null || !matricula.equals(form.getMatricula())) {
            return "redirect:/verificar-matricula";
        }
        
        if (result.hasErrors()) {
            return "registro";
        }
        
        // Verificar se o email já existe
        if (usuarioService.emailExiste(form.getEmail())) {
            result.rejectValue("email", "error.email", "Este email já está em uso");
            return "registro";
        }
        
        // Criar o usuário com os dados do formulário
        Usuario usuario = new Usuario();
        usuario.setNome(form.getNome());
        usuario.setEmail(form.getEmail());
        usuario.setSenha(form.getSenha());
        usuario.setMatricula(matricula);
        usuario.setTipoUsuario((TipoUsuario) session.getAttribute("tipoUsuario"));
        usuario.setAtivo(true);
        
        // Extrair o ano de ingresso da matrícula (assumindo que os primeiros 4 dígitos são o ano)
        try {
            String anoStr = matricula.substring(0, 4);
            usuario.setAnoIngresso(Integer.parseInt(anoStr));
        } catch (Exception e) {
            // Se não conseguir extrair o ano, usa o ano atual
            usuario.setAnoIngresso(java.time.LocalDate.now().getYear());
        }
        
        usuarioService.salvar(usuario);
        
        // Marcar a matrícula como já registrada
        verificacaoService.marcarComoRegistrada(matricula);
        
        // Limpar atributos da sessão
        session.removeAttribute("matriculaValidada");
        session.removeAttribute("nomePreCadastrado");
        session.removeAttribute("emailPreCadastrado");
        session.removeAttribute("tipoUsuario");
        
        return "redirect:/login";
    }
}