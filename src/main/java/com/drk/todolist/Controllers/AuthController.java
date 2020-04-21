package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public boolean signup(@RequestBody UserDTO userDTO) {
        return userService.signup(userDTO);
    }

    @PostMapping("/signin")
    public String signin(@RequestBody SigninDTO signinDTO) {
        if (userService.isCanLogin(signinDTO))
            return jwtTokenProvider.coreateToken(signinDTO.getUserName());
        else
            return "false";
    }
}