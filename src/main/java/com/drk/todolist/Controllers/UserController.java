package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserExistException;

import org.springframework.beans.factory.annotation.Autowired;
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


@RestController("/user/{userId}")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private UserEntity userEntity;

    @GetMapping()
    public UserInfoDTO getUserInfo(
            Authentication authentication,
            @PathParam("userId") int userId
        ){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userEntity = (UserEntity) authentication.getPrincipal();
        userInfoDTO.setNickName(userEntity.getNickname());
        userInfoDTO.setUserName(userEntity.getUsername());
        return userInfoDTO;
    }

    @PutMapping()
    public String updateUserInfo(
            HttpServletRequest request, 
            Authentication authentication, 
            @PathParam("userId") int userId,
            @RequestBody @Valid UpdateUserDTO updateUserDTO, 
            Errors errors)
            throws RequestDataInvalidException, UserExistException{
        if(errors.hasErrors())
            throw new RequestDataInvalidException("잘못된 요청 값", request, "PUT(/user)");
        try{
            String newUsername = userService.userUpdate((UserEntity) authentication.getPrincipal(), userId, updateUserDTO);
            return jwtTokenProvider.coreateToken(newUsername);
        } catch (UserExistException e){
            throw new UserExistException("이미 존재하는 username", request, "PUT(/user)");
        }
    }

    @PatchMapping()
    public boolean modifyPassword(
        Authentication authentication, 
        @PathParam("userId") int userId,
        @RequestParam String password
    ){
        return userService.modifyPassowrd((UserEntity) authentication.getPrincipal(), userId, password);
    }

    @DeleteMapping()
    public boolean withdraw(
            Authentication authentication, 
            @PathParam("userId") int userId){
        userEntity = (UserEntity) authentication.getPrincipal();
        return userService.userDelete(userEntity, userId);
    }
}