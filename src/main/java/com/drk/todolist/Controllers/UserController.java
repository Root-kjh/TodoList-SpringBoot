package com.drk.todolist.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("/")
    public String index(){
        return "User/index";
    }

    @GetMapping("/signin")
    public String signinForm() {
        return "signinForm";
    }

    @PostMapping("/signin")
    public void signinProcess(){

    }

    @GetMapping("/signup")
    public String signupForm(){
        return "signupForm";
    }

    @PostMapping("/signup")
    public void signupProcess(){

    }

    @GetMapping("/logout")
    public void logout(){

    }

    @GetMapping("/withdraw")
    public String withdrawForm(){
        return "withdrawForm";
    }

    @PostMapping("/withdraw")
    public void withdrawProcess(){

    }
}