package com.drk.todolist.Config.Controller;

public class UrlMapper {
    public static class Auth{
        public final static String baseUrl = "/auth";
        public final static String signup = "/signup";
        public final static String signin = "/signin";
    }

    public static class User{
        public final static String baseUrl = "/user";
        public final static String withdraw = "/withdraw";
        public final static String getUserInfo = "/get_user_info";
        public final static String updateUserInfo = "/update_user_info";
    }

    public static class Todo{
        public final static String baseUrl = "/todo";
        public final static String showTodoList = "/show";
        public final static String insertTodo = "/insert";
        public final static String deleteTodo = "/delete";
        public final static String updateTodo = "/update";
    }
}