package com.drk.todolist.Config.Errors;

import lombok.Getter;

@Getter
public class RequestDataInvalidException extends RuntimeException{
    
    final private String errorMessage = "RequestDataInvalid";
    final private int errorCode = 405;

    @Getter
    private ErrorConfig errorConfig;

    public RequestDataInvalidException(String requestData, String url){
        this.errorConfig = new ErrorConfig(this.errorCode, requestData, url);
    }   
}