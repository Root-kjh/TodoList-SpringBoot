package com.drk.todolist.Service;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceTestConfigure {

    @Autowired
    protected UserService userService;

    @Autowired
    protected TodoService todoService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TodoRepository todoRepository;

    protected UserEntity testUserEntity;
    protected TodoEntity testTodoEntity;

    protected TestLib testLib;

    @BeforeEach
    public void settingLibs(){
        testLib = new TestLib(userRepository, todoRepository);
    }

    @BeforeEach
    @AfterEach
    public void clearDB(){
        userRepository.deleteAll();
        todoRepository.deleteAll();
    }

}