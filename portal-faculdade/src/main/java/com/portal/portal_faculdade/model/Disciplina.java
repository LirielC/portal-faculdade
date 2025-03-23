package com.portal.portal_faculdade.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disciplinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private Integer cargaHoraria;

    @Column(nullable = false)
    private String sala;

    @Column(nullable = false)
    private String diaSemana;

    @Column(nullable = false)
    private LocalTime horarioInicio;

    @Column(nullable = false)
    private LocalTime horarioFim;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Usuario professor;

    @ManyToMany(mappedBy = "disciplinas")
    private List<Curso> cursos = new ArrayList<>();

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL)
    private List<Matricula> matriculas = new ArrayList<>();
}