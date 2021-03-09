package com.drk.todolist.Services.User;

import javax.transaction.Transactional;

import com.drk.todolist.Config.Errors.NotLoginedException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
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


    private final JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    AuthenticationManager authenticationManager;

    public boolean isExistUser(String user_name) {
        return userRepository.isExistUser(user_name);
    }

    @Override
    @Transactional
    public boolean signup(SignupDTO signupDTO) throws UserExistException{
        if (userRepository.isExistUser(signupDTO.getUserName()))
            throw new UserExistException();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signupDTO.getUserName());
        userEntity.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        userEntity.setNickname(signupDTO.getNickName());
        userRepository.save(userEntity);
        return true;

    }

    @Override
    @Transactional
    public String userUpdate(UserEntity loginedUser, int userId, UpdateUserDTO updateUserDTO) 
            throws UserExistException{
        if (!loginedUser.getUsername().equals(updateUserDTO.getUserName()))
            if (userRepository.isExistUser(updateUserDTO.getUserName()))
                throw new UserExistException();
            else
                loginedUser.setUsername(updateUserDTO.getUserName());

        loginedUser.setNickname(updateUserDTO.getNickName());
        
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
    public boolean userDelete(UserEntity loginedUser, int userId){
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
    public UserInfoDTO signin(SigninDTO signinDTO) throws NotLoginedException{
            UserEntity userEntity = userRepository.findByUsername(signinDTO.getUserName());
            if (VariablesLib.isSet(userEntity) && passwordEncoder.matches(signinDTO.getPassword(), userEntity.getPassword())){
                UserInfoDTO userInfoDTO = new UserInfoDTO(
                    userEntity.getIdx(), 
                    userEntity.getUsername(), 
                    userEntity.getNickname(), 
                    jwtTokenProvider.coreateToken(userEntity.getUsername())
                );
                return userInfoDTO;
            }else
                throw new NotLoginedException();
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
    @Transactional
    public boolean modifyPassowrd(UserEntity loginedUser, int userId, String password) {
        try{
            loginedUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(loginedUser);
            return true;
        } catch (Exception e){
            String errorMsg = "loginedUser : "+loginedUser+", newPassword : "+password;
            LogLib.ErrorLogging(errorMsg, e);
            return false;
        }
    }
}