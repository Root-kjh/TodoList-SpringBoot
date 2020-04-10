package com.drk.todolist.Controllers;

import javax.servlet.http.HttpSession;

import com.drk.todolist.DTO.UserSessionDTO;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("user")
public class UserController {

    UserSessionDTO userSessionDTO;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index(){
        return "Content/User/index";
    }

    @GetMapping("/signin")
    public String signinForm() {
        return "Content/User/signin";
    }

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

    @GetMapping("/signup")
    public String signupForm(){
        return "Content/User/signup";
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

    @GetMapping("/logout")
    public String logout(HttpSession session){
        userService.logout(session);
        return "redirect:/";
    }

    @GetMapping("/withdraw")
    public String withdrawForm(){
         return "Content/User/withdrawForm";
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

    @GetMapping("/updateUserInfo")
    public String updateUserInfoForm(HttpSession session, Model model){
        try{
            userSessionDTO = userService.getUserSession(session);
        }catch(Exception e){
            logout(session);
            return "alertMessage/User/updateUserInfoFail";
        }
        model.addAttribute("userName", userSessionDTO.getUserName());
        model.addAttribute("nickName", userSessionDTO.getUserNickName());
        return "Content/User/updateUserInfoForm";
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfoProcess(
            HttpSession session,
            @RequestParam String newUserName,
            @RequestParam String newNickName,
            @RequestParam String newPassword,
            @RequestParam String password
    ){
        if (userService.userinfoUpdate(session, newUserName, newNickName, newPassword, password))
            return "alertMessage/User/updateUserInfoSuccess";
        else
            return "alertMessage/User/updateUserInfoFail";
    }
}