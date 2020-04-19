package com.drk.todolist.Services.User;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServicelmpl implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    
    UserJwtDTO userJwtDTO;

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    @Override
    public String userinfoUpdate(UserEntity loginedUser, UserInfoDTO newUserInfoDTO) {
        try {
            newUserInfoDTO.setPassword(passwordEncoder.encode(newUserInfoDTO.getPassword()));
            String loginedUserPassword = loginedUser.getPassword();
            if (loginedUserPassword.equals(newUserInfoDTO.getPassword())) {
                if (isSet(newUserInfoDTO.getNickName()))
                    loginedUser.setNickname(newUserInfoDTO.getNickName());
                if (isSet(newUserInfoDTO.getUserName()) && !userRepository.isExistUser(newUserInfoDTO.getUserName())) {
                    loginedUser.setUsername(newUserInfoDTO.getUserName());
                }
                if (isSet(newUserInfoDTO.getPassword()))
                    loginedUser.setPassword(newUserInfoDTO.getPassword());

                userRepository.save(loginedUser);
                return loginedUser.getUsername();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    @Override
    public boolean userinfoDelete(UserEntity loginedUser, String password) {
        try {
            Long userIdx = loginedUser.getIdx();
            if (loginedUser.getPassword().equals(password) && userRepository.deleteByIdx(userIdx))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    @Override
    public boolean isCanLogin(SigninDTO signinDTO){
        try{
            UserEntity userEntity = userRepository.findByUsername(signinDTO.getUsername());
            return (userEntity != null && userEntity.getPassword().equals(passwordEncoder.encode(signinDTO.getPassword())));
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);       
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}