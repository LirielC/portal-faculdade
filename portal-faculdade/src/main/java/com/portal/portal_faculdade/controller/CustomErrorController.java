package com.portal.portal_faculdade.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        logger.error("Erro na aplicação - Status: {}, Mensagem: {}, Exceção: {}", 
                    status, message, exception);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("mensagem", "Página não encontrada.");
                return "error";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("mensagem", "Acesso negado. Você não tem permissão para acessar esta página.");
                return "error";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("mensagem", "Erro interno do servidor. Por favor, tente novamente mais tarde.");
                return "error";
            }
        }

        model.addAttribute("mensagem", message != null ? message.toString() : "Ocorreu um erro inesperado. Por favor, tente novamente.");
        return "error";
    }
} 