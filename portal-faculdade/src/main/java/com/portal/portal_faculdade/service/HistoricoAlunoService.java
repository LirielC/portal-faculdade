package com.portal.portal_faculdade.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.HistoricoAluno;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.repository.HistoricoAlunoRepository;

@Service
public class HistoricoAlunoService {

    @Autowired
    private HistoricoAlunoRepository historicoRepository;

    @Transactional
    public HistoricoAluno adicionarDisciplinaConcluida(Usuario aluno, Disciplina disciplina, double nota, int semestre, int ano) {
        // Verifica se a disciplina já está no histórico
        if (historicoRepository.existsByAlunoAndDisciplina(aluno, disciplina)) {
            throw new RuntimeException("Esta disciplina já está registrada no histórico");
        }

        HistoricoAluno historico = new HistoricoAluno();
        historico.setAluno(aluno);
        historico.setDisciplina(disciplina);
        historico.setNota1(nota);
        historico.setNota2(nota);
        historico.setNotaFinal(nota);
        historico.setSemestreAno(ano + "." + semestre);
        historico.setStatus(nota >= 7.0 ? "APROVADO" : "REPROVADO");
        historico.setDataConclusao(LocalDateTime.now());

        return historicoRepository.save(historico);
    }

    public List<HistoricoAluno> buscarHistoricoAluno(Usuario aluno) {
        return historicoRepository.findByAlunoOrderBySemestreAnoDesc(aluno);
    }

    public double calcularCR(Usuario aluno) {
        return historicoRepository.calcularCR(aluno);
    }
} 