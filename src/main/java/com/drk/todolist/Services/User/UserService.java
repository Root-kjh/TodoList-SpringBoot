package com.drk.todolist.Services.User;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public boolean signup(UserDTO userDTO) throws Exception;
    public boolean isCanLogin(SigninDTO signinDTO) throws Exception;
    public String userinfoUpdate(UserEntity loginedUser, UserDTO newUserDTO) throws Exception;
    public boolean userinfoDelete(UserEntity loginedUser, String password) throws Exception;
    public UserEntity findUserByUsername(String username) throws Exception;
}