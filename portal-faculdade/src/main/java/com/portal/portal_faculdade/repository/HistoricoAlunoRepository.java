package com.portal.portal_faculdade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.HistoricoAluno;
import com.portal.portal_faculdade.model.Usuario;

@Repository
public interface HistoricoAlunoRepository extends JpaRepository<HistoricoAluno, Long> {
    
    @Query("SELECT h FROM HistoricoAluno h JOIN FETCH h.disciplina WHERE h.aluno = :aluno ORDER BY h.semestreAno DESC")
    List<HistoricoAluno> findByAlunoOrderBySemestreAnoDesc(@Param("aluno") Usuario aluno);
    
    @Query("SELECT COUNT(h) > 0 FROM HistoricoAluno h WHERE h.aluno = :aluno AND h.disciplina = :disciplina")
    boolean existsByAlunoAndDisciplina(@Param("aluno") Usuario aluno, @Param("disciplina") Disciplina disciplina);
    
    @Query("SELECT COALESCE(AVG(h.notaFinal), 0.0) FROM HistoricoAluno h WHERE h.aluno = :aluno AND h.status = 'APROVADO'")
    double calcularCR(@Param("aluno") Usuario aluno);
} 