package com.drk.todolist.Config.Controller;

public class UrlMapper {
    public static class Auth{
        public final static String baseUrl = "/auth";
        public final static String signup = baseUrl+"/signup";
        public final static String signin = baseUrl+"/signin";
    }

    public static class User{
        public final static String baseUrl = "/user";
        public final static String withdraw = baseUrl+"/withdraw";
        public final static String getUserInfo = baseUrl+"/get_user_info";
        public final static String updateUserInfo = baseUrl+"/update_user_info";
    }

    public static class Todo{
        public final static String baseUrl = "/todo";
        public final static String showTodoList = baseUrl+"/show";
        public final static String insertTodo = baseUrl+"/insert";
        public final static String deleteTodo = baseUrl+"/delete";
        public final static String updateTodo = baseUrl+"/update";
    }
}