package com.portal.portal_faculdade.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.HistoricoAluno;
import com.portal.portal_faculdade.model.Matricula;
import com.portal.portal_faculdade.model.Presenca;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.repository.DisciplinaRepository;
import com.portal.portal_faculdade.service.HistoricoAlunoService;
import com.portal.portal_faculdade.service.MatriculaService;
import com.portal.portal_faculdade.service.PresencaService;
import com.portal.portal_faculdade.service.UsuarioService;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
    
    private static final Logger logger = LoggerFactory.getLogger(AlunoController.class);
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private MatriculaService matriculaService;
    
    @Autowired
    private HistoricoAlunoService historicoService;
    
    @Autowired
    private PresencaService presencaService;
    
    @GetMapping("/disciplinas")
    public String listarDisciplinas(Authentication authentication, Model model) {
        try {
            logger.info("Iniciando listagem de disciplinas");
            
            Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
            
            // Carrega os dados com limite
            List<Matricula> matriculas = matriculaService.listarMatriculasAtivas(aluno).stream()
                .limit(10)  // Limita a 10 matrículas por página
                .toList();
                
            List<Disciplina> disciplinasDisponiveis = matriculaService.listarDisciplinasDisponiveis(aluno).stream()
                .limit(10)  // Limita a 10 disciplinas por página
                .toList();
            
            // Adiciona atributos ao modelo
            model.addAttribute("usuario", aluno);
            model.addAttribute("matriculas", matriculas);
            model.addAttribute("disciplinasDisponiveis", disciplinasDisponiveis);
            
            logger.info("Página de disciplinas carregada com sucesso - Matrículas: {} - Disciplinas: {}", 
                       matriculas.size(), disciplinasDisponiveis.size());
                       
            return "aluno/disciplinas";
            
        } catch (Exception e) {
            logger.error("Erro ao carregar página de disciplinas: {}", e.getMessage(), e);
            model.addAttribute("erro", "Erro ao carregar as disciplinas. Por favor, tente novamente.");
            return "redirect:/aluno/perfil";
        }
    }

    @GetMapping("/historico")
    public String mostrarHistorico(Authentication authentication, Model model) {
        Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
        
        // Busca histórico completo
        List<HistoricoAluno> historico = historicoService.buscarHistoricoAluno(aluno);
        model.addAttribute("historico", historico);
        
        // Calcula CR
        double cr = historicoService.calcularCR(aluno);
        model.addAttribute("cr", cr);
        
        return "aluno/historico";
    }

    @GetMapping("/presencas/{matriculaId}")
    public String mostrarPresencas(@PathVariable Long matriculaId, Model model) {
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Matricula matricula = matriculaService.buscarMatricula(matriculaId, usuario);
            
            List<Presenca> presencas = presencaService.listarPresencas(matricula);
            double percentualPresenca = presencaService.calcularPercentualPresenca(matricula);
            
            model.addAttribute("usuario", usuario);
            model.addAttribute("matricula", matricula);
            model.addAttribute("presencas", presencas);
            model.addAttribute("percentualPresenca", percentualPresenca);
            
            return "aluno/presencas";
        } catch (RuntimeException e) {
            return "redirect:/aluno/disciplinas?erro=" + e.getMessage();
        }
    }

    @PostMapping("/matricular/{disciplinaId}")
    public String matricular(Authentication authentication, 
                           @PathVariable Long disciplinaId,
                           RedirectAttributes redirectAttributes) {
        logger.info("Tentativa de matrícula - Disciplina ID: {}", disciplinaId);
        try {
            // Verifica autenticação
            if (authentication == null) {
                logger.error("Usuário não autenticado");
                redirectAttributes.addFlashAttribute("erro", "Usuário não autenticado. Por favor, faça login novamente.");
                return "redirect:/login";
            }

            // Carrega usuário
            Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
            if (aluno == null) {
                logger.error("Usuário não encontrado");
                redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado. Por favor, faça login novamente.");
                return "redirect:/login";
            }
            logger.info("Aluno autenticado: {}", aluno.getNome());
            
            // Busca disciplina
            Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> {
                    logger.error("Disciplina não encontrada - ID: {}", disciplinaId);
                    return new RuntimeException("Disciplina não encontrada");
                });
            logger.info("Disciplina encontrada: {}", disciplina.getNome());
            
            // Tenta realizar a matrícula
            matriculaService.inscreverEmDisciplina(aluno, disciplina);
            logger.info("Matrícula realizada com sucesso - Aluno: {} - Disciplina: {}", 
                       aluno.getNome(), disciplina.getNome());
            
            redirectAttributes.addFlashAttribute("mensagem", "Matrícula realizada com sucesso!");
            return "redirect:/aluno/disciplinas";
        } catch (RuntimeException e) {
            logger.error("Erro ao realizar matrícula: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/aluno/disciplinas";
        }
    }

    @PostMapping("/cancelar/{disciplinaId}")
    public String cancelarMatricula(Authentication authentication, @PathVariable Long disciplinaId, 
            RedirectAttributes redirectAttributes) {
        try {
            Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
            matriculaService.cancelarMatricula(aluno, disciplinaId);
            redirectAttributes.addFlashAttribute("mensagem", "Matrícula cancelada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/aluno/disciplinas";
    }

    @PostMapping("/trocar")
    public String trocarDisciplina(Authentication authentication, 
            @RequestParam Long disciplinaAtualId,
            @RequestParam Long novaDisciplinaId,
            RedirectAttributes redirectAttributes) {
        try {
            Usuario aluno = (Usuario) usuarioService.loadUserByUsername(authentication.getName());
            matriculaService.trocarDisciplina(aluno, disciplinaAtualId, novaDisciplinaId);
            redirectAttributes.addFlashAttribute("mensagem", "Troca de disciplina realizada com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/aluno/disciplinas";
    }

    @PostMapping("/presencas/registrar/{matriculaId}")
    @ResponseBody
    public ResponseEntity<?> registrarPresenca(@PathVariable Long matriculaId, @RequestParam boolean presente) {
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Matricula matricula = matriculaService.buscarMatricula(matriculaId, usuario);
            
            presencaService.registrarPresenca(matricula, presente);
            
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 