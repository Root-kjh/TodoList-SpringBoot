package com.drk.todolist.DTO;

public class jwtDTO {
    public static final String SECRET = "SECRET_Todo_Key";
    public static final int EXPIRATION_TIME = 3600000; //1 Hour
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
}