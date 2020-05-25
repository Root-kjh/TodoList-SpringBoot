package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import javax.servlet.http.HttpServletRequest;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private UserEntity userEntity;

    @PostMapping(UrlMapper.User.withdraw)
    public boolean withdraw(HttpServletRequest request, Authentication authentication, @RequestParam String password, Errors errors){
        if(errors.hasErrors())
        throw new RequestDataInvalidException(request.getParameterMap().toString(), UrlMapper.User.withdraw);
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            return userService.userinfoDelete(userEntity, password);
        }catch (NullPointerException e) {
            log.debug(e.getMessage());
            throw new UserDataInvalidException(request.getParameterMap().toString(), UrlMapper.User.withdraw);
        }catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    @GetMapping(UrlMapper.User.getUserInfo)
    public UserDTO getUserInfo(HttpServletRequest request, Authentication authentication){
        UserDTO userDTO = new UserDTO();
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            userDTO.setNickName(userEntity.getNickname());
            userDTO.setUserName(userEntity.getUsername());
            return userDTO;
        } catch(NullPointerException e) {
            log.debug(e.getMessage());
            throw new UserDataInvalidException(request.getParameterMap().toString(), UrlMapper.User.getUserInfo);
        } catch(Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @PostMapping(UrlMapper.User.updateUserInfo)
    public String updateUserInfo(HttpServletRequest request, Authentication authentication, @RequestBody UserDTO newUserDTO, Errors errors){
        if(errors.hasErrors())
            throw new RequestDataInvalidException(request.getParameterMap().toString(), UrlMapper.User.updateUserInfo);
        try{
            String newUsername = userService.userinfoUpdate((UserEntity) authentication.getPrincipal(), newUserDTO);
            return jwtTokenProvider.coreateToken(newUsername);
        }catch (Exception e) {
            e.printStackTrace();
            return "false";
        }              
    }
}