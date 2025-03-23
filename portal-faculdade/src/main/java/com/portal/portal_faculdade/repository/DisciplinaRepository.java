package com.portal.portal_faculdade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.Usuario;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    List<Disciplina> findByProfessor(Usuario professor);
    List<Disciplina> findByNomeContainingIgnoreCase(String nome);
}