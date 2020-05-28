package com.drk.todolist.Controllers;

import com.drk.todolist.Config.Errors.ErrorConfig;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ErrorController{
    
    @ExceptionHandler(UserDataInvalidException.class)
    public String userDataInvalidException(UserDataInvalidException exception) {
        log.error(exception.getErrorConfig().toString());
        return exception.getErrorMessage();
    }

    @ExceptionHandler(RequestDataInvalidException.class)
    public String requestDataInvalidException(RequestDataInvalidException exception) {
        log.error(exception.getErrorConfig().toString());
        return exception.getErrorMessage();
    }

    @ExceptionHandler(UserExistException.class)
    public String requestDataInvalidException(UserExistException exception) {
        log.error(exception.getErrorConfig().toString());
        return exception.getErrorMessage();
    }
}