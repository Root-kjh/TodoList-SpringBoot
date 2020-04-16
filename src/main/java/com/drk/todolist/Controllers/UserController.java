package com.drk.todolist.Controllers;

import javax.servlet.http.HttpSession;

import com.drk.todolist.DTO.UserInfoDTO;
import com.drk.todolist.DTO.UserSessionDTO;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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

    UserSessionDTO userSessionDTO;

    @Autowired
    UserService userService;

    @PostMapping("/signin")
    public String signinProcess(
            HttpSession session, 
            @RequestParam String userName, 
            @RequestParam String password){
        if (userService.signin(session, userName, password)){
            return "alertMessage/User/signinSuccess";
        }else{
            return "alertMessage/User/signinFail";
        }
    }

    @PostMapping("/signup")
    public String signupProcess(
            @RequestParam String userName, 
            @RequestParam String nickName, 
            @RequestParam String password){
        if (userService.signup(userName, password, nickName))
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
        try{
            userSessionDTO = userService.getUserSession(session);
        }catch(Exception e){
            logout(session);
            return "alertMessage/User/updateUserInfoFail";
        }
        UserInfoDTO userInfoDTO = userService.getUserInfoByUserName(userSessionDTO.getUserName());
        return userInfoDTO;
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfoProcess(
            HttpSession session,
            @RequestParam UserInfoDTO newUserInfoDTO,
            @RequestParam String password
    ){
        if (userService.userinfoUpdate(session, newUserInfoDTO, password))
            return "alertMessage/User/updateUserInfoSuccess";
        else
            return "alertMessage/User/updateUserInfoFail";
    }
}