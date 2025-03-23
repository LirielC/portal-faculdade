package com.portal.portal_faculdade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.service.UsuarioService;

@Controller
@RequestMapping("/aluno")
public class CalendarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/calendario")
    public String mostrarCalendario(Authentication authentication, Model model) {
        Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
        model.addAttribute("usuario", aluno);
        // TODO: Adicionar lógica para buscar eventos do calendário
        return "aluno/calendario";
    }
} 