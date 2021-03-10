package com.drk.todolist.Services.User;

import com.drk.todolist.Config.Errors.LoginFailedException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public boolean signup(SignupDTO signupDTO) throws UserExistException;
    public UserInfoDTO signin(SigninDTO signinDTO) throws LoginFailedException;
    public UserInfoDTO getUserInfo(UserEntity loginedUser);
    public UserInfoDTO userUpdate(UserEntity loginedUser, UpdateUserDTO updateUserDTO);
    public boolean userDelete(UserEntity loginedUser);
    public UserEntity findUserByUsername(String username);
    public boolean modifyPassowrd(UserEntity loginedUser, String password);
}