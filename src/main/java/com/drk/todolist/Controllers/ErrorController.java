package com.drk.todolist.Controllers;

import com.drk.todolist.Config.Errors.ErrorConfig;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController{
    
    @ExceptionHandler(UserDataInvalidException.class)
    public ErrorConfig userDataInvalidException(UserDataInvalidException exception) {
        return exception.getErrorConfig();
    }

    @ExceptionHandler(RequestDataInvalidException.class)
    public ErrorConfig requestDataInvalidException(RequestDataInvalidException exception) {
        return exception.getErrorConfig();
    }
}