package com.drk.todolist.Config.Errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;  

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestDataInvalidException.class)
    public String handleRequestDataInvalidException(RequestDataInvalidException e){
        return "{'Message': 'Request Data Invalid'}";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserDataInvalidException.class)
    public String handleUserDataInvalidException(UserDataInvalidException e){
        return "{'Messaage': 'Permission Denied'}";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserExistException.class)
    public String handleUserExiseExcecption(UserExistException e){
        return "{'Message': 'User Exist'}";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailedException.class)
    public String handleLoginFailedException(LoginFailedException e){
        return "{'Message': 'Login Failed'}";
    }
}
