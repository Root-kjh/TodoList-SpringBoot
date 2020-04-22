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

public class ServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    TodoService todoService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    UserEntity testUserEntity;
    TodoEntity testTodoEntity;

    TestLib testLib;

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