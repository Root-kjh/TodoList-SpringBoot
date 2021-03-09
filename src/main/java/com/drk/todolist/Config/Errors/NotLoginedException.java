package com.drk.todolist.Config.Errors;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;

@Getter
public class NotLoginedException extends BusinessException{
    
    private static final long serialVersionUID = 1L;
    final private String errorMessage = "NotLogined";
    final private int errorCode = 400;

    public NotLoginedException(){}

    public NotLoginedException(HttpServletRequest request){
        super("Not Logined", request, "/auth/signin");
    }   
}