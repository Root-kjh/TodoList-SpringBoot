package com.drk.todolist.Config.Errors;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;  

@RestControllerAdvice
public class GlobalExceptionHandler{
    
    @ExceptionHandler(RequestDataInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRequestDataInvalidException(){
        return "{\"Message\": \"Request Data Invalid\"}";
    }

    @ExceptionHandler(UserDataInvalidException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleUserDataInvalidException(){
        return "{\"Message\": \"Permission Denied\"}";
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserExiseExcecption(){
        return "{\"Message\": \"User Exist\"}";
    }

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLoginFailedException(){
        return "{\"Message\": \"Login Failed\"}";
    }
}
