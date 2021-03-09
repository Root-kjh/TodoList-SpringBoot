package com.drk.todolist.Config.Errors;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;

@Getter
public class RequestDataInvalidException extends BusinessException{
    
    private static final long serialVersionUID = 1L;
    final private String errorMessage = "RequestDataInvalid";
    final private int errorCode = 405;

    public RequestDataInvalidException(){}
    
    public RequestDataInvalidException(HttpServletRequest request, String url){
        super("Request Data Invalid", request, url);
    }   

}