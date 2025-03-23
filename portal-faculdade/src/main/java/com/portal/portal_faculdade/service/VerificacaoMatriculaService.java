package com.portal.portal_faculdade.service;

import org.springframework.stereotype.Service;

import com.portal.portal_faculdade.model.MatriculaAutorizada;
import com.portal.portal_faculdade.repository.MatriculaAutorizadaRepository;

@Service
public class VerificacaoMatriculaService {
    
    private final MatriculaAutorizadaRepository matriculaRepository;
    
    public VerificacaoMatriculaService(MatriculaAutorizadaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }
    
    public boolean verificarMatricula(String matricula) {
        return matriculaRepository.existsByMatricula(matricula) && 
               !matriculaRepository.findByMatricula(matricula).get().isJaRegistrado();
    }
    
    public MatriculaAutorizada getMatriculaAutorizada(String matricula) {
        return matriculaRepository.findByMatricula(matricula).orElse(null);
    }
    
    public void marcarComoRegistrada(String matricula) {
        MatriculaAutorizada ma = matriculaRepository.findByMatricula(matricula).orElse(null);
        if (ma != null) {
            ma.setJaRegistrado(true);
            matriculaRepository.save(ma);
        }
    }
}