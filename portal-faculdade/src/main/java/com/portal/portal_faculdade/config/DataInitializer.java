package com.portal.portal_faculdade.config;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.portal.portal_faculdade.model.Disciplina;
import com.portal.portal_faculdade.model.TipoUsuario;
import com.portal.portal_faculdade.model.Usuario;
import com.portal.portal_faculdade.repository.DisciplinaRepository;
import com.portal.portal_faculdade.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            
            if (disciplinaRepository.count() == 0) {
                // Criar professores
                Usuario profWeb = criarProfessor("Dr. João Silva", "joao.silva@faculdade.com", "PROF001");
                Usuario profBD = criarProfessor("Dra. Maria Santos", "maria.santos@faculdade.com", "PROF002");
                Usuario profAlg = criarProfessor("Dr. Pedro Oliveira", "pedro.oliveira@faculdade.com", "PROF003");
                Usuario profPOO = criarProfessor("Dra. Ana Costa", "ana.costa@faculdade.com", "PROF004");
                Usuario profRedes = criarProfessor("Dr. Carlos Ferreira", "carlos.ferreira@faculdade.com", "PROF005");
                Usuario profSO = criarProfessor("Dra. Lucia Martins", "lucia.martins@faculdade.com", "PROF006");

               
                Disciplina programacaoWeb = new Disciplina();
                programacaoWeb.setNome("Programação Web");
                programacaoWeb.setDescricao("Desenvolvimento de aplicações web com Spring Boot");
                programacaoWeb.setCargaHoraria(60);
                programacaoWeb.setSala("Lab 101");
                programacaoWeb.setDiaSemana("Segunda-feira");
                programacaoWeb.setHorarioInicio(LocalTime.of(14, 0));
                programacaoWeb.setHorarioFim(LocalTime.of(16, 0));
                programacaoWeb.setProfessor(profWeb);
                disciplinaRepository.save(programacaoWeb);

                
                Disciplina bancoDados = new Disciplina();
                bancoDados.setNome("Banco de Dados");
                bancoDados.setDescricao("Fundamentos de bancos de dados relacionais");
                bancoDados.setCargaHoraria(60);
                bancoDados.setSala("Lab 102");
                bancoDados.setDiaSemana("Terça-feira");
                bancoDados.setHorarioInicio(LocalTime.of(16, 0));
                bancoDados.setHorarioFim(LocalTime.of(18, 0));
                bancoDados.setProfessor(profBD);
                disciplinaRepository.save(bancoDados);

               
                Disciplina algoritmos = new Disciplina();
                algoritmos.setNome("Algoritmos e Estruturas de Dados");
                algoritmos.setDescricao("Estudo de algoritmos e estruturas de dados fundamentais");
                algoritmos.setCargaHoraria(60);
                algoritmos.setSala("Lab 103");
                algoritmos.setDiaSemana("Quarta-feira");
                algoritmos.setHorarioInicio(LocalTime.of(8, 0));
                algoritmos.setHorarioFim(LocalTime.of(10, 0));
                algoritmos.setProfessor(profAlg);
                disciplinaRepository.save(algoritmos);

               
                Disciplina poo = new Disciplina();
                poo.setNome("Programação Orientada a Objetos");
                poo.setDescricao("Conceitos e práticas de programação orientada a objetos");
                poo.setCargaHoraria(60);
                poo.setSala("Lab 104");
                poo.setDiaSemana("Quinta-feira");
                poo.setHorarioInicio(LocalTime.of(10, 0));
                poo.setHorarioFim(LocalTime.of(12, 0));
                poo.setProfessor(profPOO);
                disciplinaRepository.save(poo);

                Disciplina redes = new Disciplina();
                redes.setNome("Redes de Computadores");
                redes.setDescricao("Fundamentos de redes e protocolos de comunicação");
                redes.setCargaHoraria(60);
                redes.setSala("Lab 105");
                redes.setDiaSemana("Sexta-feira");
                redes.setHorarioInicio(LocalTime.of(14, 0));
                redes.setHorarioFim(LocalTime.of(16, 0));
                redes.setProfessor(profRedes);
                disciplinaRepository.save(redes);

                
                Disciplina so = new Disciplina();
                so.setNome("Sistemas Operacionais");
                so.setDescricao("Conceitos e implementação de sistemas operacionais");
                so.setCargaHoraria(60);
                so.setSala("Lab 106");
                so.setDiaSemana("Segunda-feira");
                so.setHorarioInicio(LocalTime.of(16, 0));
                so.setHorarioFim(LocalTime.of(18, 0));
                so.setProfessor(profSO);
                disciplinaRepository.save(so);
            }
        };
    }

    private Usuario criarProfessor(String nome, String email, String matricula) {
        Usuario professor = new Usuario();
        professor.setNome(nome);
        professor.setEmail(email);
        professor.setMatricula(matricula);
        professor.setSenha(passwordEncoder.encode("senha123")); // Senha padrão inicial
        professor.setTipoUsuario(TipoUsuario.PROFESSOR);
        professor.setAtivo(true);
        
      
        if (nome.startsWith("Dr.")) {
            professor.setTitulacao("Doutor em Ciência da Computação");
        } else if (nome.startsWith("Dra.")) {
            professor.setTitulacao("Doutora em Ciência da Computação");
        }
        
        return usuarioRepository.save(professor);
    }
} 
