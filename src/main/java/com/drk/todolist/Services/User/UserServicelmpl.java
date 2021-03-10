package com.drk.todolist.Services.User;

import javax.transaction.Transactional;

import com.drk.todolist.Config.Errors.LoginFailedException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
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
    public UserInfoDTO signin(SigninDTO signinDTO) throws LoginFailedException{
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
                throw new LoginFailedException();
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
    public UserInfoDTO userUpdate(UserEntity loginedUser, UpdateUserDTO updateUserDTO) {
        loginedUser.setNickname(updateUserDTO.getNickName());
        userRepository.save(loginedUser);
        String jwt = jwtTokenProvider.coreateToken(loginedUser.getUsername());
        UserInfoDTO userInfoDTO = new UserInfoDTO(loginedUser.getIdx(), loginedUser.getUsername(), loginedUser.getNickname(), jwt);
        return userInfoDTO;
    }

    @Override
    @Transactional
    public boolean userDelete(UserEntity loginedUser){
        UserEntity userEntity = userRepository.findByUsername(loginedUser.getUsername());
        userRepository.deleteByIdx(userEntity.getIdx());
        return true;
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
    public boolean modifyPassowrd(UserEntity loginedUser, String password) {
        loginedUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(loginedUser);
        return true;
    }

    @Override
    public UserInfoDTO getUserInfo(UserEntity loginedUser){
        String jwt = jwtTokenProvider.coreateToken(loginedUser.getUsername()); 
        UserInfoDTO userInfoDTO = new UserInfoDTO(loginedUser.getIdx(), loginedUser.getUsername(), loginedUser.getNickname(), jwt);
        return userInfoDTO;
    }
}