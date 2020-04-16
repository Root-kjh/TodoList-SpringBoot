package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.DTO.User.UserJwtDTO;

public interface UserService {
    public boolean signin(HttpSession session, SigninDTO signinDTO);
    public boolean signup(UserInfoDTO userInfoDTO);
    public boolean logout(HttpSession session);
    public boolean userinfoUpdate(HttpSession session, UserInfoDTO newUserInfoDTO, String password);
    public boolean userinfoDelete(HttpSession session, String password);
    public UserJwtDTO getUserInfoByJWT(HttpSession session) throws Exception;
    public UserInfoDTO getUserInfoByUserName(String userName) throws Exception;
}