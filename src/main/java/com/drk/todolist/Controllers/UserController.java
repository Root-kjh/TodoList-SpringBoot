package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private UserEntity userEntity;

    @PostMapping("/withdraw")
    public boolean withdraw(Authentication authentication, @RequestParam String password){
        userEntity = (UserEntity) authentication.getPrincipal();
        return userService.userinfoDelete(userEntity, password);

    }

    @GetMapping("/getUserInfo")
    public UserInfoDTO getUserInfo(Authentication authentication){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            userInfoDTO.setNickName(userEntity.getNickname());
            userInfoDTO.setUserName(userEntity.getUsername());
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            return userInfoDTO;
        }
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfo(Authentication authentication, @RequestBody UserInfoDTO newUserInfoDTO){
        String newUsername = userService.userinfoUpdate((UserEntity) authentication.getPrincipal(), newUserInfoDTO);
        if(newUsername != null)
            return jwtTokenProvider.coreateToken(newUsername);
        else
            return "false";              
    }
}