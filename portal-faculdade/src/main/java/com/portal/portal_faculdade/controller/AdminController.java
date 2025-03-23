package com.portal.portal_faculdade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.portal_faculdade.service.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/redefinir-senha")
    public String mostrarPaginaRedefinirSenha() {
        return "admin/redefinir-senha";
    }

    @PostMapping("/redefinir-senha")
    public String redefinirSenha(@RequestParam String matricula, 
                                @RequestParam String novaSenha,
                                RedirectAttributes redirectAttributes) {
        try {
            usuarioService.redefinirSenha(matricula, novaSenha);
            redirectAttributes.addFlashAttribute("mensagem", "Senha redefinida com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao redefinir senha: " + e.getMessage());
            return "redirect:/admin/redefinir-senha";
        }
    }
} 