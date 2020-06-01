package com.drk.todolist.Config.Errors;

import lombok.Getter;

@Getter
public class UserExistException extends RuntimeException{
    
    final private String errorMessage = "UserExist";
    final private int errorCode = 406;

    @Getter
    private ErrorConfig errorConfig;

    public UserExistException(String requestData, String url){
        this.errorConfig = new ErrorConfig(this.errorCode, requestData, url);
    }   

    public UserExistException(){}
}