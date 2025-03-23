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
@Table(name = "presencas")
public class Presenca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;
    
    @Column(name = "data_aula", nullable = false)
    private LocalDateTime dataAula;
    
    @Column(nullable = false)
    private boolean presente;
    
    @Column(name = "total_aulas")
    private Integer totalAulas;
    
    @Column(name = "aulas_presentes")
    private Integer aulasPresentes;
    
    @Column(name = "percentual_presenca")
    private Double percentualPresenca;
} 