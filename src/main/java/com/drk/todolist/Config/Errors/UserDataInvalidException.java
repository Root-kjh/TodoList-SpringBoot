package com.drk.todolist.Config.Errors;

import lombok.Getter;

@Getter
public class UserDataInvalidException extends RuntimeException {

    final private String errorMessage = "UserDataInvalid";
    final private int errorCode = 403;

    @Getter
    private ErrorConfig errorConfig;

    public UserDataInvalidException(String requestData, String url){
        this.errorConfig = new ErrorConfig(this.errorCode, requestData, url);
    }
}