package com.drk.todolist.Services.User;

import com.drk.todolist.Config.Errors.NotLoginedException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public boolean signup(SignupDTO signupDTO) throws UserExistException;
    public UserInfoDTO signin(SigninDTO signinDTO) throws NotLoginedException;
    public UserInfoDTO getUserInfo(UserEntity userEntity, int userId) throws 
    public String userUpdate(UserEntity loginedUser, int userId, UpdateUserDTO updateUserDTO) throws UserExistException;
    public boolean userDelete(UserEntity loginedUser, int userId);
    public UserEntity findUserByUsername(String username);
    public boolean modifyPassowrd(UserEntity loginedUser, int userId, String password);
}