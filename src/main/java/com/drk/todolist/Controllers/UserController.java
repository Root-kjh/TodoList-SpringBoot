package com.drk.todolist.Controllers;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import javax.servlet.http.HttpServletRequest;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
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
    public boolean withdraw(HttpServletRequest request, Authentication authentication, @RequestParam String password, Errors errors){
        try{
            if(errors.hasErrors())
                throw new RequestDataInvalidException();
            userEntity = (UserEntity) authentication.getPrincipal();
            return userService.userinfoDelete(userEntity, password);
        }catch (NullPointerException e) {
            throw new UserDataInvalidException(request.getParameterMap().toString(), UrlMapper.User.withdraw);
        }catch (Exception e){
            e.printStackTrace();
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
            throw new UserDataInvalidException(request.getParameterMap().toString(), UrlMapper.User.getUserInfo);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(UrlMapper.User.updateUserInfo)
    public String updateUserInfo(HttpServletRequest request, Authentication authentication, @RequestBody UserDTO newUserDTO, Errors errors){
        try{
            if(errors.hasErrors())
                throw new RequestDataInvalidException();
            String newUsername = userService.userinfoUpdate((UserEntity) authentication.getPrincipal(), newUserDTO);
            return jwtTokenProvider.coreateToken(newUsername);
        } catch (RequestDataInvalidException e){
            throw new RequestDataInvalidException();
        } catch (UserExistException e) {
            throw new UserExistException();
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }              
    }
}