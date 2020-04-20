package com.drk.todolist.Services.User;

import javax.transaction.Transactional;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

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

    public boolean isSet(Object object) {
        return object != null;
    }

    public boolean isExistUser(String user_name) {
        return userRepository.isExistUser(user_name);
    }

    @Override
    public boolean signup(UserInfoDTO userInfoDTO) {
        try {
            if (!userRepository.isExistUser(userInfoDTO.getUserName())){
                userInfoDTO.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(userInfoDTO.getUserName());
                userEntity.setPassword(userInfoDTO.getPassword());
                userEntity.setNickname(userInfoDTO.getNickName());
                userRepository.save(userEntity);
                return true;
            }
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String userinfoUpdate(UserEntity loginedUser, UserInfoDTO newUserInfoDTO) {
        try {
            if (isSet(newUserInfoDTO.getPassword()))
                loginedUser.setPassword(passwordEncoder.encode(newUserInfoDTO.getPassword()));
            if (isSet(newUserInfoDTO.getNickName()))
                loginedUser.setNickname(newUserInfoDTO.getNickName());
            if (isSet(newUserInfoDTO.getUserName()) && !userRepository.isExistUser(newUserInfoDTO.getUserName()))
                loginedUser.setUsername(newUserInfoDTO.getUserName());
            
            userRepository.save(loginedUser);
            return loginedUser.getUsername();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public boolean userinfoDelete(UserEntity loginedUser, String password) {
        try {
            UserEntity userEntity = userRepository.findByUsername(loginedUser.getUsername());
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                userRepository.deleteByIdx(userEntity.getIdx());
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isCanLogin(SigninDTO signinDTO){
        try{
            UserEntity userEntity = userRepository.findByUsername(signinDTO.getUserName());
            return (userEntity != null && passwordEncoder.matches(signinDTO.getPassword(), userEntity.getPassword()));
        }catch (Exception e){
            return false;
        }
    }
    
    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}