package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;

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

import com.drk.todolist.lib.ControllerLib;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @PostMapping(UrlMapper.Auth.signup)
    public boolean signup(HttpServletRequest request, @RequestBody @Valid UserDTO userDTO, Errors errors) {
        try{
            if (errors.hasErrors())
                throw new RequestDataInvalidException();
            if(userService.signup(userDTO))
                return true;
            else
                throw new UserExistException();
        } catch (RequestDataInvalidException e){
            throw new UserDataInvalidException(ControllerLib.getRequestBodyToString(request),UrlMapper.Auth.signup);
        } catch (UserExistException e){
            throw new UserExistException(ControllerLib.getRequestBodyToString(request),UrlMapper.Auth.signup);
        } catch (Exception e){
             e.printStackTrace();
             return false;
        }
    }

    @PostMapping(UrlMapper.Auth.signin)
    public String signin(HttpServletRequest request, @RequestBody @Valid SigninDTO signinDTO,Errors errors) {
        try{
            if (errors.hasErrors())
                throw new RequestDataInvalidException();
            if (userService.isCanLogin(signinDTO))
                return jwtTokenProvider.coreateToken(signinDTO.getUserName());
            else
                throw new UserDataInvalidException();
        } catch (RequestDataInvalidException e){
            throw new RequestDataInvalidException(ControllerLib.getRequestBodyToString(request),UrlMapper.Auth.signup);
        } catch (UserDataInvalidException e){
            throw new UserDataInvalidException(ControllerLib.getRequestBodyToString(request),UrlMapper.Auth.signup);
        } catch(Exception e) {
            e.printStackTrace();
            return "false";
        }
    }
}