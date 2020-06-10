package com.drk.todolist.Config;

import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;

public class ServiceTest extends TestInit {

    @Autowired
    protected UserService userService;

    @Autowired
    protected TodoService todoService;

}