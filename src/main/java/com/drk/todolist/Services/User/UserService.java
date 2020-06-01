package com.drk.todolist.Services.User;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public boolean signup(SignupDTO signupDTO) throws Exception;
    public boolean isCanLogin(SigninDTO signinDTO) throws Exception;
    public String userinfoUpdate(UserEntity loginedUser, UpdateUserDTO updateUserDTO) throws Exception;
    public boolean userinfoDelete(UserEntity loginedUser, String password) throws Exception;
    public UserEntity findUserByUsername(String username) throws Exception;
    public boolean modifyPassowrd(UserEntity loginedUser, String newPassword) throws Exception;
}