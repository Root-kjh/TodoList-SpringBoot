package com.drk.todolist.Services.JWT;

import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.lib.LogLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        try{
        return userRepository.findByUsername(username);       
        } catch (final Exception e){
            String errorMsg = "userName : "+username;
            LogLib.ErrorLogging(errorMsg, e);
            return null;
        }
    }
}