package com.drk.todolist.Config.JWT;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.JWT.JwtTokenUtil;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        
        String userName = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            try{
                userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                try{
                    UserEntity userEntity = this.userService.getUserInfoByUserName(userName);
                    UserJwtDTO userJwtDTO = new UserJwtDTO();
                    
                    userJwtDTO.setUserIdx(userEntity.getIdx());
                    userJwtDTO.setUserNickName(userEntity.getNickname());
                    userJwtDTO.setUserName(userEntity.getUsername());

                    if (jwtTokenUtil.validateToken(jwtToken, userJwtDTO)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userJwtDTO, null);
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }

                } catch (Exception e){
                    System.out.println("User not find");
                }
            }
        }
        filterChain.doFilter(request, response);
    }   
}