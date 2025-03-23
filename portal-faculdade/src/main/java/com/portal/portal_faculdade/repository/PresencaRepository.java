package com.portal.portal_faculdade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.portal.portal_faculdade.model.Matricula;
import com.portal.portal_faculdade.model.Presenca;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    List<Presenca> findByMatriculaOrderByDataAulaDesc(Matricula matricula);
    
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.matricula = ?1 AND p.presente = true")
    Long countPresencasByMatricula(Matricula matricula);
    
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.matricula = ?1")
    Long countTotalAulasByMatricula(Matricula matricula);
} 