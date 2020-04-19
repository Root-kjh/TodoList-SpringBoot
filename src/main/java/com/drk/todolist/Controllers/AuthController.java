package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @PostMapping("signup")
    public boolean signup(@RequestBody UserInfoDTO userInfoDTO) {
        return userService.signup(userInfoDTO);
    }

    @PostMapping("signin")
    public String signin(@RequestBody SigninDTO signinDTO) {
        if (userService.isCanLogin(signinDTO))
            return jwtTokenProvider.coreateToken(signinDTO.getUsername());
        else
            return "false";
    }
}