package com.drk.todolist.Config.Errors;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;

@Getter
public class UserExistException extends BusinessException{
    
    private static final long serialVersionUID = 1L;
    final private String errorMessage = "UserExist";
    final private int errorCode = 406;

    public UserExistException(){}

    public UserExistException(HttpServletRequest request, String url){
        super("User Exist", request, url);
    }   
}