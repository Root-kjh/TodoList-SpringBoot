package com.drk.todolist.Services.User;

import javax.transaction.Transactional;

import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.lib.VariablesLib;
import com.drk.todolist.lib.LogLib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServicelmpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    UserRepository userRepository;


    @Autowired
    AuthenticationManager authenticationManager;

    public boolean isExistUser(String user_name) {
        return userRepository.isExistUser(user_name);
    }

    @Override
    public boolean signup(SignupDTO signupDTO) throws UserExistException{
        if (userRepository.isExistUser(signupDTO.getUserName()))
            throw new UserExistException();
        try{
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(signupDTO.getUserName());
            userEntity.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
            userEntity.setNickname(signupDTO.getNickName());
            userRepository.save(userEntity);
            return true;
        } catch (Exception e){
            String errorMsg = "signupDTO : "+signupDTO.toString();
            LogLib.ErrorLogging(errorMsg, e);
            return false;
        }
    }

    @Override
    public String userinfoUpdate(UserEntity loginedUser, UpdateUserDTO updateUserDTO) 
            throws UserExistException{
        if (!loginedUser.getUsername().equals(updateUserDTO.getNewUserName()))
            if (userRepository.isExistUser(updateUserDTO.getNewUserName()))
                throw new UserExistException();
            else
                loginedUser.setUsername(updateUserDTO.getNewUserName());

        loginedUser.setNickname(updateUserDTO.getNewNickName());
        
        try{
            userRepository.save(loginedUser);
            return loginedUser.getUsername();
        } catch (Exception e){
            String errorMsg = "loginedUser : "+loginedUser.toString()+", updateUserDTO : "+updateUserDTO.toString();
            LogLib.ErrorLogging(errorMsg, e);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean userinfoDelete(UserEntity loginedUser, String password){
        try{
            UserEntity userEntity = userRepository.findByUsername(loginedUser.getUsername());
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                userRepository.deleteByIdx(userEntity.getIdx());
                return true;
            } else
                return false;
        } catch (Exception e){
            String errorMsg = "loginedUser : "+loginedUser.toString()+", password : "+password;
            LogLib.ErrorLogging(errorMsg, e);
            return false;
        }
    }

    @Override
    public boolean isCanLogin(SigninDTO signinDTO) {
        try{
            UserEntity userEntity = userRepository.findByUsername(signinDTO.getUserName());
            return (VariablesLib.isSet(userEntity) &&
                passwordEncoder.matches(signinDTO.getPassword(), userEntity.getPassword()));
        } catch (Exception e){
            String errorMsg = "signinDTO : "+signinDTO.toString();
            LogLib.ErrorLogging(errorMsg, e);
            return false;
        }
    }
    
    @Override
    public UserEntity findUserByUsername(String username) {
        try{
            return userRepository.findByUsername(username);
        } catch (Exception e){
            String errorMsg = "username : "+username;
            LogLib.ErrorLogging(errorMsg, e);
            return null;
        }
    }

    @Override
    public boolean modifyPassowrd(UserEntity loginedUser, String newPassword) {
        try{
            loginedUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(loginedUser);
            return true;
        } catch (Exception e){
            String errorMsg = "loginedUser : "+loginedUser+", newPassword : "+newPassword;
            LogLib.ErrorLogging(errorMsg, e);
            return false;
        }
    }
}