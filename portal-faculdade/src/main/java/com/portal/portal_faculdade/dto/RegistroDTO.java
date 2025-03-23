package com.portal.portal_faculdade.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroDTO {
    
    @NotEmpty(message = "O nome é obrigatório")
    private String nome;
    
    @NotEmpty(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotEmpty(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;
    
    @NotEmpty(message = "A confirmação de senha é obrigatória")
    private String confirmacaoSenha;
}