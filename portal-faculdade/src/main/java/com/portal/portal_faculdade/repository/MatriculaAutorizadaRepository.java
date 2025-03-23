package com.portal.portal_faculdade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.portal_faculdade.model.MatriculaAutorizada;

@Repository
public interface MatriculaAutorizadaRepository extends JpaRepository<MatriculaAutorizada, Long> {
    Optional<MatriculaAutorizada> findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
}