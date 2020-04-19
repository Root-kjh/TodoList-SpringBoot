package com.drk.todolist.Service;

import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;

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
    
    final String testUserName = "testUser";
    final String testUserNickName = "test";
    final String testUserPassword = "test123!";

    final String testTitle = "testTodo";
    final String testContext = "testContext";

    @BeforeEach
    @AfterEach
    public void clearUserDB(){
        userRepository.deleteAll();
    }

}