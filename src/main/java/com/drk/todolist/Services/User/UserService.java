package com.drk.todolist.Services.User;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public boolean signup(UserDTO userDTO);
    public boolean isCanLogin(SigninDTO signinDTO);
    public String userinfoUpdate(UserEntity loginedUser, UserDTO newUserDTO);
    public boolean userinfoDelete(UserEntity loginedUser, String password);
    public UserEntity findUserByUsername(String username);
}