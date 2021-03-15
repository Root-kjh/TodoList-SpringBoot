package com.drk.todolist.Controllers;

import com.drk.todolist.DTO.User.ModifyPasswordDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;

import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/user/{userId}")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private UserEntity userEntity;

    private final String successMessage = "{\"Message\": \"Success\"}";

    public UserEntity userPermissionCheck(Authentication authentication, Long userId) throws Exception{
        userEntity = (UserEntity) authentication.getPrincipal();
        if (userEntity.getIdx().equals(userId))
            return userEntity;
        else
            throw new UserDataInvalidException();
    }

    @GetMapping()
    public UserInfoDTO getUserInfo(
            Authentication authentication,
            @PathVariable("userId") Long userId
        ) throws Exception{
        userEntity = userPermissionCheck(authentication, userId);
        UserInfoDTO userInfoDTO = userService.getUserInfo(userEntity);
        return userInfoDTO;
    }

    @PutMapping()
    public UserInfoDTO updateUserInfo(
            HttpServletRequest request, 
            Authentication authentication, 
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UpdateUserDTO updateUserDTO, 
            Errors errors) throws Exception{
        if(errors.hasErrors())
            throw new RequestDataInvalidException();
        userEntity = userPermissionCheck(authentication, userId);
        UserInfoDTO newUserInfo = userService.userUpdate(userEntity, updateUserDTO);
        return newUserInfo;
    }

    @PatchMapping()
    public String modifyPassword(
        Authentication authentication, 
        @PathVariable("userId") Long userId,
        @RequestBody @Valid ModifyPasswordDTO passwordDTO
    ) throws Exception{
        userEntity = userPermissionCheck(authentication, userId);
        userService.modifyPassowrd(userEntity, passwordDTO.getPassword());
        return this.successMessage;
    }

    @DeleteMapping()
    public String withdraw(
            Authentication authentication, 
            @PathVariable("userId") Long userId) throws Exception{
        userEntity = userPermissionCheck(authentication, userId);
        userService.userDelete(userEntity);
        return this.successMessage;
    }
}