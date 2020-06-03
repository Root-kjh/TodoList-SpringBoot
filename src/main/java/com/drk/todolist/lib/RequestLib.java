package com.drk.todolist.lib;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestLib {
    public static String requestToString(HttpServletRequest request){
        try{
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(request.getParameterMap());
        return requestBody;
        } catch(JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}