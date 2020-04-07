package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    public boolean signin(HttpSession session, String userName, String password);
    public boolean signup(String userName, String password, String nickName);
    public boolean logout(HttpSession session);
    public boolean userinfoUpdate(HttpSession session, String newUserName, String newNickName, String newPassword, String password);
    public boolean userinfoDelete(HttpSession session, String password);
}