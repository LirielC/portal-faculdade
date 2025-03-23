package com.portal.portal_faculdade.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portal.portal_faculdade.model.Matricula;
import com.portal.portal_faculdade.model.Presenca;
import com.portal.portal_faculdade.repository.PresencaRepository;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;

    @Transactional
    public void registrarPresenca(Matricula matricula, boolean presente) {
        LocalDateTime dataAula = LocalDateTime.now();
        
        Presenca presenca = new Presenca();
        presenca.setMatricula(matricula);
        presenca.setDataAula(dataAula);
        presenca.setPresente(presente);
        
        // Atualiza estatísticas
        long totalAulas = presencaRepository.countTotalAulasByMatricula(matricula) + 1;
        long aulasPresentes = presencaRepository.countPresencasByMatricula(matricula) + (presente ? 1 : 0);
        
        presenca.setTotalAulas((int) totalAulas);
        presenca.setAulasPresentes((int) aulasPresentes);
        presenca.setPercentualPresenca((double) aulasPresentes / totalAulas * 100);
        
        presencaRepository.save(presenca);
    }

    public List<Presenca> listarPresencas(Matricula matricula) {
        return presencaRepository.findByMatriculaOrderByDataAulaDesc(matricula);
    }

    public Double calcularPercentualPresenca(Matricula matricula) {
        Long totalAulas = presencaRepository.countTotalAulasByMatricula(matricula);
        if (totalAulas == 0) {
            return 0.0;
        }
        Long presencas = presencaRepository.countPresencasByMatricula(matricula);
        return (double) presencas / totalAulas * 100;
    }
} 