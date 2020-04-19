package com.drk.todolist.Services.User;

import com.drk.todolist.Entitis.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class getUserEntityByJwt {

    @Autowired
    UserService userService;

    public final UserEntity getUserEntity(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByUsername(username);
        }
}