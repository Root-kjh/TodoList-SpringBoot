package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @PostMapping(UrlMapper.Auth.signup)
    public boolean signup(HttpServletRequest request, @RequestBody @Valid SignupDTO signupDTO, Errors errors)
            throws RequestDataInvalidException, UserExistException {
        boolean isSignUp;
        if (errors.hasErrors())
            throw new RequestDataInvalidException("잘못된 요청 값", request, UrlMapper.Auth.signup);
        try{
            isSignUp = userService.signup(signupDTO);
        } catch (UserExistException e){
            throw new UserExistException("이미 존재하는 유저", request, UrlMapper.Auth.signup);
        }
        if (isSignUp)
            return true;
        else
            return false;
    }

    @PostMapping(UrlMapper.Auth.signin)
    public String signin(HttpServletRequest request, @RequestBody @Valid SigninDTO signinDTO,Errors errors) 
            throws RequestDataInvalidException, UserDataInvalidException{
        if (errors.hasErrors())
            throw new RequestDataInvalidException("잘못된 요청 값", request, UrlMapper.Auth.signin);
        if (userService.isCanLogin(signinDTO))
            return jwtTokenProvider.coreateToken(signinDTO.getUserName());
        else
            throw new UserDataInvalidException("로그인 실패", request, UrlMapper.Auth.signin);
    }
}