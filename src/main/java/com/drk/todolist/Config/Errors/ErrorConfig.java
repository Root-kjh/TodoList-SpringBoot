package com.drk.todolist.Config.Errors;

public class ErrorConfig {
    private int errorCode;
    private String reqeustData;
    private String url;
    private String errorMessage;

    ErrorConfig(int errorCode, String requestData, String url, String errorMessage){
        this.errorCode = errorCode;
        this.reqeustData = requestData;
        this.url = url;
        this.errorMessage = errorMessage;
    }

}