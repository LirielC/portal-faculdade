package com.portal.portal_faculdade.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/error/**", "/webjars/**", "/h2-console/**").permitAll()
                .requestMatchers("/", "/login", "/registro", "/verificar-matricula", "/admin/redefinir-senha").permitAll()
                .requestMatchers("/aluno/**").hasAuthority("ALUNO")
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions().disable()) // Para o console do H2
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("matricula")
                .passwordParameter("senha")
                .defaultSuccessUrl("/aluno/dashboard", true)
                .failureHandler((request, response, exception) -> {
                    logger.error("Falha na autenticação: {}", exception.getMessage());
                    String errorMessage;
                    if (exception instanceof BadCredentialsException) {
                        errorMessage = "Matrícula ou senha inválidos";
                    } else if (exception instanceof DisabledException) {
                        errorMessage = "Conta desativada";
                    } else if (exception instanceof LockedException) {
                        errorMessage = "Conta bloqueada";
                    } else {
                        errorMessage = "Erro ao fazer login. Por favor, tente novamente";
                    }
                    response.sendRedirect("/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
                })
                .successHandler((request, response, authentication) -> {
                    logger.info("Login bem-sucedido para usuário: {}", authentication.getName());
                    response.sendRedirect("/aluno/dashboard");
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}