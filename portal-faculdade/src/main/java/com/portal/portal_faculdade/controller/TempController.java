package com.portal.portal_faculdade.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {
    
    private final PasswordEncoder passwordEncoder;
    
    public TempController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/generate-password")
    public String generatePassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
} 