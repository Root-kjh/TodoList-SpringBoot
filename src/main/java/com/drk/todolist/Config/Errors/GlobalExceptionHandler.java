package com.drk.todolist.Config.Errors;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;  

@RestControllerAdvice
public class GlobalExceptionHandler{
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestDataInvalidException.class)
    public String handleRequestDataInvalidException(){
        return "{'Message': 'Request Data Invalid'}";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserDataInvalidException.class)
    public String handleUserDataInvalidException(){
        return "{'Messaage': 'Permission Denied'}";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistException.class)
    public String handleUserExiseExcecption(){
        return "{'Message': 'User Exist'}";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailedException.class)
    public String handleLoginFailedException(){
        return "{'Message': 'Login Failed'}";
    }
}
