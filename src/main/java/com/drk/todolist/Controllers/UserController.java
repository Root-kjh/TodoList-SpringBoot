package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private UserEntity userEntity;

    @PostMapping(UrlMapper.User.withdraw)
    public boolean withdraw(
            Authentication authentication, 
            @RequestParam String password){
        userEntity = (UserEntity) authentication.getPrincipal();
        return userService.userinfoDelete(userEntity, password);
    }

    @GetMapping(UrlMapper.User.getUserInfo)
    public UserInfoDTO getUserInfo(
            Authentication authentication){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userEntity = (UserEntity) authentication.getPrincipal();
        userInfoDTO.setNickName(userEntity.getNickname());
        userInfoDTO.setUserName(userEntity.getUsername());
        return userInfoDTO;
    }

    @PostMapping(UrlMapper.User.updateUserInfo)
    public String updateUserInfo(
            HttpServletRequest request, 
            Authentication authentication, 
            @RequestBody @Valid UpdateUserDTO updateUserDTO, 
            Errors errors)
            throws RequestDataInvalidException, UserExistException{
        if(errors.hasErrors())
            throw new RequestDataInvalidException("잘못된 요청 값", request, UrlMapper.User.updateUserInfo);
        try{
            String newUsername = userService.userinfoUpdate((UserEntity) authentication.getPrincipal(), updateUserDTO);
            return jwtTokenProvider.coreateToken(newUsername);
        } catch (UserExistException e){
            throw new UserExistException("이미 존재하는 username", request, UrlMapper.User.updateUserInfo);
        }
    }

    @PostMapping(UrlMapper.User.modifyPassword)
    public boolean modifyPassword(Authentication authentication, @RequestParam String newPassword){
        return userService.modifyPassowrd((UserEntity) authentication.getPrincipal(), newPassword);
    }
}