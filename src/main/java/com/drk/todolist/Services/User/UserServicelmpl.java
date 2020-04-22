package com.drk.todolist.Services.User;

import javax.transaction.Transactional;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.lib.VariablesLib;

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
    public boolean signup(UserDTO userDTO) {
        try {
            if (!userRepository.isExistUser(userDTO.getUserName())){
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(userDTO.getUserName());
                userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                userEntity.setNickname(userDTO.getNickName());
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
    public String userinfoUpdate(UserEntity loginedUser, UserDTO newUserDTO) {
        try {
            if (isSet(newUserDTO.getPassword()))
                loginedUser.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
            if (isSet(newUserDTO.getNickName()))
                loginedUser.setNickname(newUserDTO.getNickName());
            if (isSet(newUserDTO.getUserName()) && !userRepository.isExistUser(newUserDTO.getUserName()))
                loginedUser.setUsername(newUserDTO.getUserName());
            
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
            System.out.println(passwordEncoder.getClass());
            UserEntity userEntity = userRepository.findByUsername(signinDTO.getUserName());
            return (VariablesLib.isSet(userEntity) && passwordEncoder.matches(signinDTO.getPassword(), userEntity.getPassword()));
        }catch (Exception e){
            return false;
        }
    }
    
    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}