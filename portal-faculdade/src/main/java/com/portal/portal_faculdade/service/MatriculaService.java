package com.portal.portal_faculdade.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.HistoricoAluno;
import com.portal.portal_faculdade.model.Matricula;
import com.portal.portal_faculdade.model.StatusMatricula;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.repository.DisciplinaRepository;
import com.portal.portal_faculdade.repository.HistoricoAlunoRepository;
import com.portal.portal_faculdade.repository.MatriculaRepository;

@Service
public class MatriculaService {

    private static final Logger logger = LoggerFactory.getLogger(MatriculaService.class);

    @Autowired
    private MatriculaRepository matriculaRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private HistoricoAlunoRepository historicoRepository;

    public static final int LIMITE_MAXIMO_MATRICULAS = 6;
    public static final int LIMITE_MINIMO_MATRICULAS = 2;
    public static final int LIMITE_MAXIMO_ALUNOS_POR_DISCIPLINA = 30;
    public static final double NOTA_MINIMA_APROVACAO = 7.0;

    public Matricula buscarMatricula(Long matriculaId, Usuario aluno) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
            .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
            
        if (!matricula.getAluno().equals(aluno)) {
            throw new RuntimeException("Esta matrícula não pertence ao aluno");
        }
        
        return matricula;
    }

    @Transactional
    public Matricula inscreverEmDisciplina(Usuario aluno, Disciplina disciplina) {
        logger.info("Iniciando processo de matrícula - Aluno: {} - Disciplina: {}", 
                   aluno.getNome(), disciplina.getNome());

        // Verifica se já está matriculado
        if (matriculaRepository.findByAlunoAndDisciplina(aluno, disciplina).isPresent()) {
            logger.warn("Tentativa de matrícula duplicada - Aluno: {} - Disciplina: {}", 
                       aluno.getNome(), disciplina.getNome());
            throw new RuntimeException("Aluno já está matriculado nesta disciplina");
        }

        // Verifica se já cursou a disciplina
        if (historicoRepository.existsByAlunoAndDisciplina(aluno, disciplina)) {
            logger.warn("Tentativa de matrícula em disciplina já cursada - Aluno: {} - Disciplina: {}", 
                       aluno.getNome(), disciplina.getNome());
            throw new RuntimeException("Aluno já cursou esta disciplina");
        }

        // Verifica limite máximo de disciplinas
        long matriculasAtivas = matriculaRepository.countByAlunoAndStatus(aluno, StatusMatricula.ATIVA);
        logger.info("Verificando limite de matrículas - Aluno: {} - Matrículas ativas: {}", 
                   aluno.getNome(), matriculasAtivas);
        
        if (matriculasAtivas >= LIMITE_MAXIMO_MATRICULAS) {
            logger.warn("Limite máximo de disciplinas atingido - Aluno: {} - Limite: {}", 
                       aluno.getNome(), LIMITE_MAXIMO_MATRICULAS);
            throw new RuntimeException("Limite máximo de " + LIMITE_MAXIMO_MATRICULAS + " disciplinas atingido");
        }

        // Verifica limite de alunos na disciplina
        long alunosMatriculados = matriculaRepository.countByDisciplinaAndStatus(disciplina, StatusMatricula.ATIVA);
        logger.info("Verificando vagas na disciplina - Disciplina: {} - Alunos matriculados: {}", 
                   disciplina.getNome(), alunosMatriculados);
        
        if (alunosMatriculados >= LIMITE_MAXIMO_ALUNOS_POR_DISCIPLINA) {
            logger.warn("Disciplina sem vagas - Disciplina: {} - Vagas preenchidas: {}", 
                       disciplina.getNome(), alunosMatriculados);
            throw new RuntimeException("Não há vagas disponíveis nesta disciplina");
        }

        // Verifica se está no período de matrícula
        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        if (mesAtual < 1 || mesAtual > 12) {
            throw new RuntimeException("Período de matrícula não está aberto");
        }

        try {
            Matricula matricula = new Matricula();
            matricula.setAluno(aluno);
            matricula.setDisciplina(disciplina);
            matricula.setStatus(StatusMatricula.ATIVA);
            matricula.setDataMatricula(hoje);
            
            // Define o semestre atual (1 para primeiro semestre, 2 para segundo semestre)
            int semestreAtual = mesAtual <= 6 ? 1 : 2;
            int anoAtual = hoje.getYear();
            matricula.setSemestreAno(anoAtual + "." + semestreAtual);
            
            Matricula matriculaSalva = matriculaRepository.save(matricula);
            logger.info("Matrícula realizada com sucesso - ID: {} - Aluno: {} - Disciplina: {}", 
                       matriculaSalva.getId(), aluno.getNome(), disciplina.getNome());
            
            return matriculaSalva;
        } catch (Exception e) {
            logger.error("Erro ao salvar matrícula - Aluno: {} - Disciplina: {} - Erro: {}", 
                        aluno.getNome(), disciplina.getNome(), e.getMessage(), e);
            throw new RuntimeException("Erro ao realizar matrícula: " + e.getMessage());
        }
    }

    @Transactional
    public void cancelarMatricula(Usuario aluno, Long disciplinaId) {
        // Verifica limite mínimo de disciplinas
        long matriculasAtivas = matriculaRepository.countByAlunoAndStatus(aluno, StatusMatricula.ATIVA);
        if (matriculasAtivas <= LIMITE_MINIMO_MATRICULAS) {
            throw new RuntimeException("Não é possível cancelar. Mínimo de " + LIMITE_MINIMO_MATRICULAS + " disciplinas obrigatório");
        }

        Matricula matricula = matriculaRepository.findByAlunoAndDisciplinaId(aluno, disciplinaId)
            .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
            
        if (matricula.getStatus() != StatusMatricula.ATIVA) {
            throw new RuntimeException("Esta matrícula não está ativa");
        }
        
        matriculaRepository.delete(matricula);
    }

    @Transactional
    public Matricula trocarDisciplina(Usuario aluno, Long disciplinaAtualId, Long novaDisciplinaId) {
        // Cancela matrícula atual
        Matricula matriculaAtual = matriculaRepository.findByAlunoAndDisciplinaId(aluno, disciplinaAtualId)
            .orElseThrow(() -> new RuntimeException("Matrícula atual não encontrada"));
            
        if (matriculaAtual.getStatus() != StatusMatricula.ATIVA) {
            throw new RuntimeException("A matrícula atual não está ativa");
        }
        
        // Inscreve na nova disciplina
        Disciplina novaDisciplina = disciplinaRepository.findById(novaDisciplinaId)
            .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
            
        // Verifica se já está matriculado na nova disciplina
        if (matriculaRepository.findByAlunoAndDisciplina(aluno, novaDisciplina).isPresent()) {
            throw new RuntimeException("Aluno já está matriculado na disciplina de destino");
        }
        
        // Verifica se já cursou a nova disciplina
        if (historicoRepository.existsByAlunoAndDisciplina(aluno, novaDisciplina)) {
            throw new RuntimeException("Aluno já cursou a disciplina de destino");
        }

        matriculaRepository.delete(matriculaAtual);
        return inscreverEmDisciplina(aluno, novaDisciplina);
    }

    @Transactional
    public void concluirDisciplina(Matricula matricula, double nota) {
        // Valida a nota
        if (nota < 0 || nota > 10) {
            throw new RuntimeException("Nota deve estar entre 0 e 10");
        }

        
        if (matricula.getStatus() != StatusMatricula.ATIVA) {
            throw new RuntimeException("Esta matrícula não está ativa");
        }

        
        HistoricoAluno historico = new HistoricoAluno();
        historico.setAluno(matricula.getAluno());
        historico.setDisciplina(matricula.getDisciplina());
        historico.setNota1(nota);
        historico.setNota2(nota);
        historico.setNotaFinal(nota);
        historico.setSemestreAno(matricula.getSemestreAno());
        historico.setStatus(nota >= NOTA_MINIMA_APROVACAO ? "APROVADO" : "REPROVADO");
        historico.setDataConclusao(LocalDateTime.now());

        historicoRepository.save(historico);

        
        matriculaRepository.delete(matricula);
    }

    public List<Matricula> listarMatriculasAtivas(Usuario aluno) {
        logger.info("Buscando matrículas ativas para o aluno: {}", aluno.getNome());
        List<Matricula> matriculas = matriculaRepository.findByAlunoAndStatus(aluno, StatusMatricula.ATIVA);
        logger.info("Encontradas {} matrículas ativas", matriculas.size());
        matriculas.forEach(m -> logger.info("Matrícula: {} - Disciplina: {}", m.getId(), m.getDisciplina().getNome()));
        return matriculas;
    }

    public List<Disciplina> listarDisciplinasDisponiveis(Usuario aluno) {
        logger.info("Buscando disciplinas disponíveis para o aluno: {}", aluno.getNome());
        
        
        List<Disciplina> todasDisciplinas = disciplinaRepository.findAll();
        logger.info("Total de disciplinas encontradas: {}", todasDisciplinas.size());
        
        
        List<Long> disciplinasMatriculadasIds = matriculaRepository.findByAlunoAndStatus(aluno, StatusMatricula.ATIVA)
            .stream()
            .map(m -> m.getDisciplina().getId())
            .toList();
        logger.info("Disciplinas matriculadas: {}", disciplinasMatriculadasIds.size());
        
        
        List<Disciplina> disciplinasDisponiveis = todasDisciplinas.stream()
            .filter(d -> !disciplinasMatriculadasIds.contains(d.getId()))
            .filter(d -> !historicoRepository.existsByAlunoAndDisciplina(aluno, d))
            .filter(d -> matriculaRepository.countByDisciplinaAndStatus(d, StatusMatricula.ATIVA) < LIMITE_MAXIMO_ALUNOS_POR_DISCIPLINA)
            .toList();
        
        logger.info("Disciplinas disponíveis: {}", disciplinasDisponiveis.size());
        disciplinasDisponiveis.forEach(d -> 
            logger.info("Disciplina disponível: {} - Professor: {}", d.getNome(), 
                       d.getProfessor() != null ? d.getProfessor().getNome() : "Sem professor"));
        
        return disciplinasDisponiveis;
    }
} 