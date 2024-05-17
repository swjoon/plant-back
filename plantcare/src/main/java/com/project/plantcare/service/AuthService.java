package com.project.plantcare.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

import com.project.plantcare.dto.LoginDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public void authenticateLogin(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = loginDTO.toAuthentication();
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }
}