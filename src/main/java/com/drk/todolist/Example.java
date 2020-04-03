package com.drk.todolist;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Example {

    @RequestMapping("/")
    @ResponseBody
    String home(){
        return "hello spring boot!";
    }
}