package com.drk.todolist.Controllers;

import javax.servlet.http.HttpSession;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    UserJwtDTO UserJwtDTO;

    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> signinProcess(
            HttpSession session, 
            @RequestParam SigninDTO SigninDTO){
        ResponseEntity.ok(new )
    }

    @PostMapping("/signup")
    public String signupProcess(
            @RequestParam UserInfoDTO userInfoDTO){
        if (userService.signup(userInfoDTO))
            return "alertMessage/User/signupSuccess";
        else
            return "alertMessage/User/signupFail";
    }

    @PostMapping("/withdraw")
    public String withdrawProcess(
            HttpSession session,
            @RequestParam String password){
                if (userService.userinfoDelete(session, password))
                    return "alertMessage/User/withdrawSuccess";
                else
                    return "alertMessage/User/withdrawFail";

    }

    @GetMapping("/getUserInfo")
    public UserInfoDTO getUserInfo(HttpSession session){
        UserInfoDTO userInfoDTO = null;
        try{
            UserJwtDTO = userService.getUserInfoByJWT(session);
            userInfoDTO = userService.getUserInfoByUserName(UserJwtDTO.getUserName());
        }catch(Exception e){
            e.printStackTrace();
        }
        return userInfoDTO;
    }

    @PostMapping("/updateUserInfo")
    public boolean updateUserInfoProcess(
            HttpSession session,
            @RequestParam UserInfoDTO newUserInfoDTO,
            @RequestParam String password
    ){
        if (userService.userinfoUpdate(session, newUserInfoDTO, password))
            return true;
        else
            return false;
    }
}