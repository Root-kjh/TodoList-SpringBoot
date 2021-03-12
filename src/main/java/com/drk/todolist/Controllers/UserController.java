package com.drk.todolist.Controllers;

import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private UserEntity userEntity;

    public UserEntity userPermissionCheck(Authentication authentication, Long userId) throws UserDataInvalidException{
        userEntity = (UserEntity) authentication.getPrincipal();
        if (userEntity.getIdx().equals(userId))
            return userEntity;
        else
            throw new UserDataInvalidException();
    }

    @GetMapping("/{userId}")
    public UserInfoDTO getUserInfo(
            Authentication authentication,
            @PathParam("userId") Long userId
        ){
        userEntity = userPermissionCheck(authentication, userId);
        UserInfoDTO userInfoDTO = userService.getUserInfo(userEntity);
        return userInfoDTO;
    }

    @PutMapping("/{userId}")
    public UserInfoDTO updateUserInfo(
            HttpServletRequest request, 
            Authentication authentication, 
            @PathParam("userId") Long userId,
            @RequestBody @Valid UpdateUserDTO updateUserDTO, 
            Errors errors){
        if(errors.hasErrors())
            throw new RequestDataInvalidException();
        userEntity = userPermissionCheck(authentication, userId);
        UserInfoDTO newUserInfo = userService.userUpdate(userEntity, updateUserDTO);
        return newUserInfo;
    }

    @PatchMapping("/{userId}")
    public String modifyPassword(
        Authentication authentication, 
        @PathParam("userId") Long userId,
        @RequestParam String password
    ){
        userEntity = userPermissionCheck(authentication, userId);
        userService.modifyPassowrd(userEntity, password);
        return "{'Message': 'Success'}";
    }

    @DeleteMapping("/{userId}")
    public String withdraw(
            Authentication authentication, 
            @PathParam("userId") Long userId){
        userEntity = userPermissionCheck(authentication, userId);
        userService.userDelete(userEntity);
        return "{'Message': 'Success'}";
    }
}