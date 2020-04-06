package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    public boolean signin(HttpSession session, String user_name, String password);
    public boolean signup(String user_name, String password, String nick_name);
    public boolean logout(HttpSession session);
    public boolean userinfoUpdate(HttpSession session, String new_user_name, String new_nick_name, String new_password, String password);
}