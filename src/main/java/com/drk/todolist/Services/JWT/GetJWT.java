package com.drk.todolist.Services.JWT;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.drk.todolist.DTO.SigninDTO;
import com.drk.todolist.DTO.jwtDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetJWT extends UsernamePasswordAuthenticationFilter{
    
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attempAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SigninDTO credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), SigninDTO.class);
        } catch (IOException e){
            e.printStackTrace();
        }
    
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(), 
                credentials.getPassword(),
                new ArrayList<>());

        Authentication auth = authenticationManager.authenticate(authenticationToken);

        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest  request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        
        String token = JWT.create()
            .withSubject(principal.getName())
            .withExpiresAt(new Date(System.currentTimeMillis() + jwtDTO.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(jwtDTO.SECRET.getBytes()));

            response.addHeader(jwtDTO.HEADER_STRING, jwtDTO.TOKEN_PREFIX+token);
    }
    
}