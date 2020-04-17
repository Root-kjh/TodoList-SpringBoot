package com.drk.todolist.Controllers;

import javax.servlet.http.HttpSession;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    UserJwtDTO UserJwtDTO;

    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody SigninDTO signinDTO){
        return ResponseEntity.ok(userService.signin(signinDTO));
    }

    @PostMapping("/signup")
    public boolean signup(
            @RequestParam UserInfoDTO userInfoDTO){
        return userService.signup(userInfoDTO);
    }

    @PostMapping("/withdraw")
    public boolean withdraw(
            @RequestParam String token,
            @RequestParam String password){
                return userService.userinfoDelete(token, password);

    }

    @GetMapping("/getUserInfo")
    public UserInfoDTO getUserInfo(@RequestParam String token){
        UserInfoDTO userInfoDTO = null;
        try{
            UserJwtDTO = userService.getUserInfoByJWT(session);
            userInfoDTO = userService.getUserInfoByUserName(UserJwtDTO.getUserName());
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            return userInfoDTO;
        }
    }

    @PostMapping("/updateUserInfo")
    public boolean updateUserInfo(@RequestBody UserInfoDTO newUserInfoDTO){
        return userService.userinfoUpdate(newUserInfoDTO);              
    }
}