package com.drk.todolist.Config.Errors;

import lombok.ToString;

@ToString
public class ErrorConfig {
    private int errorCode;
    private String reqeustData;
    private String url;

    ErrorConfig(int errorCode, String requestData, String url){
        this.errorCode = errorCode;
        this.reqeustData = requestData;
        this.url = url;
    }

}