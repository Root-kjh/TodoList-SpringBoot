package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @PostMapping(UrlMapper.Auth.signup)
    public boolean signup(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO, Errors errors) {
        if (errors.hasErrors())
            throw new RequestDataInvalidException(request.getParameterMap().toString(), UrlMapper.Auth.signup);
        try{
            return userService.signup(userDTO);
        }catch(Exception e) {
            log.error(e.getMessage());
            return false;
        }     
    }

    @PostMapping(UrlMapper.Auth.signin)
    public String signin(HttpServletRequest request, @RequestBody @Valid SigninDTO signinDTO,Errors errors) {
        if (errors.hasErrors())
            throw new RequestDataInvalidException(request.getParameterMap().toString(), UrlMapper.Auth.signin);
        try{
            if (userService.isCanLogin(signinDTO))
                return jwtTokenProvider.coreateToken(signinDTO.getUserName());
            else
                throw new UserDataInvalidException(request.getParameterMap().toString(), UrlMapper.Auth.signup);
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new UserDataInvalidException(request.getParameterMap().toString(), UrlMapper.Auth.signup);        
        }
    }
}