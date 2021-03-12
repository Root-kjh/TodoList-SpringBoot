package com.drk.todolist.Controllers;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.Config.Errors.LoginFailedException;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.Services.User.UserService;

import org.json.simple.JSONObject;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public JSONObject signup(HttpServletRequest request, @RequestBody @Valid SignupDTO signupDTO, Errors errors)
        throws RequestDataInvalidException, UserExistException{
        if (errors.hasErrors())
            throw new RequestDataInvalidException();
        userService.signup(signupDTO);
        JSONObject response = new JSONObject();
        response.put("Message", "Success");
        return response;
    }

    @PostMapping("/signin")
    public UserInfoDTO signin(HttpServletRequest request, @RequestBody @Valid SigninDTO signinDTO, Errors errors)
        throws RequestDataInvalidException, LoginFailedException{
        if (errors.hasErrors())
            throw new RequestDataInvalidException();
        return userService.signin(signinDTO);
    }
}