package com.drk.todolist.Config;

import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;


public class UnitTest extends TestInit {

    protected UserService userService;

    protected TodoService todoService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    
    @Autowired
    public void setTodoService(TodoService todoService){
        this.todoService = todoService;
    }
}