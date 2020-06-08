package com.drk.todolist.Controllers;

import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ErrorController{
    
    @ExceptionHandler(UserDataInvalidException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String userDataInvalidException(UserDataInvalidException exception) {
        log.error(exception.getMessage());
        return exception.getErrorMessage();
    }

    @ExceptionHandler(RequestDataInvalidException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public String requestDataInvalidException(RequestDataInvalidException exception) {
        log.error(exception.getMessage());
        return exception.getErrorMessage();
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public String userExsistException(UserExistException exception) {
        log.error(exception.getMessage());
        return exception.getErrorMessage();
    }
}