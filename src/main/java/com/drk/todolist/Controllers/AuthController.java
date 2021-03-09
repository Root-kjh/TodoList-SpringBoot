package com.drk.todolist.Controllers;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.Config.Errors.NotLoginedException;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.Services.User.UserService;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public boolean signup(HttpServletRequest request, @RequestBody @Valid SignupDTO signupDTO, Errors errors)
            throws RequestDataInvalidException, UserExistException {
        if (errors.hasErrors())
            throw new RequestDataInvalidException(request, "/auth/signup");
        try {
            userService.signup(signupDTO);
            return true;
        } catch (UserExistException e) {
            throw new UserExistException(request, "/auth/signup");
        }
    }

    @PostMapping("/signin")
    public UserInfoDTO signin(HttpServletRequest request, @RequestBody @Valid SigninDTO signinDTO, Errors errors)
            throws RequestDataInvalidException, NotLoginedException {
        if (errors.hasErrors())
            throw new RequestDataInvalidException(request, "/auth/signin");
        try {
            return userService.signin(signinDTO);
        } catch (NotLoginedException e) {
            throw new NotLoginedException(request);
        }
    }
}