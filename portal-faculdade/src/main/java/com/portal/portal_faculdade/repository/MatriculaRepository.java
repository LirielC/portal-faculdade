package com.portal.portal_faculdade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.Matricula;
import com.portal.portal_faculdade.model.StatusMatricula;
import com.portal.portal_faculdade.model.Usuario;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByAluno(Usuario aluno);
    List<Matricula> findByDisciplina(Disciplina disciplina);
    List<Matricula> findByAlunoAndStatus(Usuario aluno, StatusMatricula status);
    List<Matricula> findByAlunoAndStatusNot(Usuario aluno, StatusMatricula status);
    Optional<Matricula> findByAlunoAndDisciplina(Usuario aluno, Disciplina disciplina);
    Optional<Matricula> findByAlunoAndDisciplinaId(Usuario aluno, Long disciplinaId);
    boolean existsByAlunoAndDisciplina(Usuario aluno, Disciplina disciplina);
    boolean existsByAlunoAndDisciplinaAndStatusNot(Usuario aluno, Disciplina disciplina, StatusMatricula status);
    long countByAlunoAndStatus(Usuario aluno, StatusMatricula status);
    long countByDisciplinaAndStatus(Disciplina disciplina, StatusMatricula status);
}