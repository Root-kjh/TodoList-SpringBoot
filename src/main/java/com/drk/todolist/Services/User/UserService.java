package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

import com.drk.todolist.DTO.SigninDTO;
import com.drk.todolist.DTO.UserInfoDTO;
import com.drk.todolist.DTO.UserJwtDTO;

public interface UserService {
    public boolean signin(HttpSession session, SigninDTO signinDTO);
    public boolean signup(UserInfoDTO userInfoDTO);
    public boolean logout(HttpSession session);
    public boolean userinfoUpdate(HttpSession session, UserInfoDTO newUserInfoDTO, String password);
    public boolean userinfoDelete(HttpSession session, String password);
    public UserJwtDTO getUserSession(HttpSession session) throws Exception;
    public UserInfoDTO getUserInfoByUserName(String userName) throws Exception;
}