package com.drk.todolist.Services.User;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public String signin(SigninDTO signinDTO);
    public boolean signup(UserInfoDTO userInfoDTO);
    public String userinfoUpdate(UserJwtDTO userJwtDTO, UserInfoDTO newUserInfoDTO);
    public boolean userinfoDelete(UserJwtDTO userJwtDTO, String password);
    public UserEntity getUserInfoByUserName(String userName) throws Exception;
}