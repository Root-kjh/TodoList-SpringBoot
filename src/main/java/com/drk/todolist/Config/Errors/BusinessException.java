package com.drk.todolist.Config.Errors;

import javax.servlet.http.HttpServletRequest;

import com.drk.todolist.lib.RequestLib;

public class BusinessException extends Exception{
    
    private static final long serialVersionUID = 1L;

    public BusinessException() {
    }
    public BusinessException(String info, HttpServletRequest request, String url){
        super(info + "Request["+RequestLib.requestToString(request)+"]"+"URL["+url+"]");
    }
}