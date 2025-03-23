package com.portal.portal_faculdade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.portal.portal_faculdade.model.MatriculaAutorizada;
import com.portal.portal_faculdade.service.VerificacaoMatriculaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/verificar-matricula")
public class VerificacaoMatriculaController {
    
    private final VerificacaoMatriculaService verificacaoService;
    
    public VerificacaoMatriculaController(VerificacaoMatriculaService verificacaoService) {
        this.verificacaoService = verificacaoService;
    }
    
    @GetMapping
    public String mostrarFormulario() {
        return "verificar-matricula";
    }
    
    @PostMapping
    public String verificarMatricula(@RequestParam String matricula, Model model, HttpSession session) {
        if (verificacaoService.verificarMatricula(matricula)) {
            // Matrícula válida, armazena na sessão e redireciona para o registro
            MatriculaAutorizada ma = verificacaoService.getMatriculaAutorizada(matricula);
            session.setAttribute("matriculaValidada", matricula);
            session.setAttribute("nomePreCadastrado", ma.getNome());
            session.setAttribute("emailPreCadastrado", ma.getEmail());
            session.setAttribute("tipoUsuario", ma.getTipoUsuario());
            
            return "redirect:/registro";
        } else {
            // Matrícula inválida ou já registrada
            model.addAttribute("erro", "Matrícula inválida ou já registrada no sistema.");
            return "verificar-matricula";
        }
    }
}