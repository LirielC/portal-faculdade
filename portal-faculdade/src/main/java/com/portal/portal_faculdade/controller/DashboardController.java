package com.portal.portal_faculdade.controller;

import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.HistoricoAluno;
import com.portal.portal_faculdade.model.Matricula;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.service.HistoricoAlunoService;
import com.portal.portal_faculdade.service.MatriculaService;
import com.portal.portal_faculdade.service.PresencaService;
import com.portal.portal_faculdade.service.UsuarioService;

@Controller
@RequestMapping("/aluno")
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MatriculaService matriculaService;
    
    @Autowired
    private HistoricoAlunoService historicoService;
    
    @Autowired
    private PresencaService presencaService;
    
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Authentication authentication, Model model) {
        try {
            logger.info("Carregando dashboard do aluno");
            
            // Carrega dados do usuário
            Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
            model.addAttribute("usuario", aluno);
            
            // Carrega matrículas ativas
            List<Matricula> matriculas = matriculaService.listarMatriculasAtivas(aluno);
            model.addAttribute("matriculas", matriculas);
            
            // Carrega histórico
            List<HistoricoAluno> historico = historicoService.buscarHistoricoAluno(aluno);
            model.addAttribute("historico", historico);
            
            // Calcula CR
            double cr = historicoService.calcularCR(aluno);
            model.addAttribute("cr", cr);
            
            // Calcula frequência média
            double frequenciaMedia = 0.0;
            if (!matriculas.isEmpty()) {
                double somaFrequencias = 0.0;
                for (Matricula m : matriculas) {
                    somaFrequencias += presencaService.calcularPercentualPresenca(m);
                }
                frequenciaMedia = somaFrequencias / matriculas.size();
            }
            model.addAttribute("frequenciaMedia", frequenciaMedia);
            
            // Busca próximas aulas
            List<Disciplina> proximasAulas = matriculas.stream()
                .map(Matricula::getDisciplina)
                .filter(d -> {
                    LocalTime agora = LocalTime.now();
                    return d.getHorarioInicio() != null && d.getHorarioInicio().isAfter(agora);
                })
                .sorted((d1, d2) -> d1.getHorarioInicio().compareTo(d2.getHorarioInicio()))
                .limit(3)
                .toList();
            model.addAttribute("proximasAulas", proximasAulas);
            
            logger.info("Dashboard carregado com sucesso - Aluno: {} - Matrículas: {} - Histórico: {}", 
                       aluno.getNome(), matriculas.size(), historico.size());
            
            return "aluno/dashboard";
            
        } catch (Exception e) {
            logger.error("Erro ao carregar dashboard: {}", e.getMessage(), e);
            model.addAttribute("erro", "Erro ao carregar dashboard: " + e.getMessage());
            return "aluno/dashboard";
        }
    }
} 