package com.drk.todolist.Services.User;

import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;

public interface UserService {
    public boolean signup(SignupDTO signupDTO) throws UserExistException;
    public boolean isCanLogin(SigninDTO signinDTO);
    public String userinfoUpdate(UserEntity loginedUser, UpdateUserDTO updateUserDTO) throws UserExistException;
    public boolean userinfoDelete(UserEntity loginedUser, String password);
    public UserEntity findUserByUsername(String username);
    public boolean modifyPassowrd(UserEntity loginedUser, String newPassword);
}