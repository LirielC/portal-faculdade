package com.portal.portal_faculdade.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "historico_aluno")
public class HistoricoAluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Usuario aluno;
    
    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;
    
    @Column(name = "nota1")
    private Double nota1;
    
    @Column(name = "nota2")
    private Double nota2;
    
    @Column(name = "nota_final")
    private Double notaFinal;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "semestre_ano")
    private String semestreAno;
    
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
} 