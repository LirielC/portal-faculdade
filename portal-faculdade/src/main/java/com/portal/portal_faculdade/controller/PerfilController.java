package com.portal.portal_faculdade.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import com.portal.portal_faculdade.service.UsuarioService;

@Controller
@RequestMapping("/aluno")
public class PerfilController {

    private static final Logger logger = Logger.getLogger(PerfilController.class.getName());

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HistoricoAlunoService historicoAlunoService;

    @Autowired
    private MatriculaService matriculaService;

    private List<Disciplina> buscarProximasAulas(List<Matricula> matriculas) {
        LocalTime agora = LocalTime.now();
        return matriculas.stream()
            .map(Matricula::getDisciplina)
            .filter(d -> d.getHorarioInicio() != null && d.getHorarioInicio().isAfter(agora))
            .limit(3)
            .toList();
    }

    private List<HistoricoAluno> criarHistoricoFicticio(Usuario aluno) {
        List<HistoricoAluno> historico = new ArrayList<>();
        
        // Disciplinas fictícias
        String[][] disciplinas = {
            {"Algoritmos e Programação", "2023.2", "8.5", "APROVADO"},
            {"Cálculo I", "2023.2", "7.0", "APROVADO"},
            {"Introdução à Computação", "2023.2", "9.0", "APROVADO"},
            {"Matemática Discreta", "2023.2", "8.0", "APROVADO"},
            {"Programação Orientada a Objetos", "2024.1", "8.7", "APROVADO"}
        };
        
        for (String[] d : disciplinas) {
            HistoricoAluno ha = new HistoricoAluno();
            Disciplina disc = new Disciplina();
            disc.setNome(d[0]);
            
            ha.setAluno(aluno);
            ha.setDisciplina(disc);
            ha.setSemestreAno(d[1]);
            double nota = Double.parseDouble(d[2]);
            ha.setNota1(nota);
            ha.setNota2(nota);
            ha.setNotaFinal(nota);
            ha.setStatus(d[3]);
            ha.setDataConclusao(LocalDateTime.now().minusMonths(2));
            
            historico.add(ha);
        }
        
        return historico;
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Authentication authentication, Model model) {
        try {
            logger.info("Iniciando acesso ao perfil...");
            
            if (authentication == null) {
                logger.severe("Erro: Authentication é null");
                return "redirect:/login";
            }
            
            // Obtém o usuário autenticado
            Usuario usuario = (Usuario) authentication.getPrincipal();
            if (usuario == null) {
                logger.severe("Erro: Usuário não encontrado");
                return "redirect:/login";
            }
            logger.info("Usuário carregado com sucesso: " + usuario.getNome());
            
            try {
                List<Matricula> matriculasAtivas = matriculaService.listarMatriculasAtivas(usuario);
                logger.info("Matrículas ativas carregadas: " + matriculasAtivas.size());
                model.addAttribute("matriculasAtivas", matriculasAtivas);
                
                List<Disciplina> proximasAulas = buscarProximasAulas(matriculasAtivas);
                logger.info("Próximas aulas encontradas: " + proximasAulas.size());
                model.addAttribute("proximasAulas", proximasAulas);
            } catch (Exception e) {
                logger.severe("Erro ao carregar matrículas: " + e.getMessage());
                e.printStackTrace();
            }
            
            try {
                List<HistoricoAluno> historico = historicoAlunoService.buscarHistoricoAluno(usuario);
                model.addAttribute("historico", historico);
                
                double cr = historicoAlunoService.calcularCR(usuario);
                logger.info("CR calculado: " + cr);
                model.addAttribute("cr", cr);
            } catch (Exception e) {
                logger.severe("Erro ao calcular CR: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("cr", 0.0);
            }
            
            model.addAttribute("usuario", usuario);
            logger.info("Retornando template aluno/perfil");
            return "aluno/perfil";
            
        } catch (Exception e) {
            logger.severe("Erro não tratado: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/error";
        }
    }
} 