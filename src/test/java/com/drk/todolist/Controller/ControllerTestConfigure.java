package com.drk.todolist.Controller;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ControllerTestConfigure {

    @Autowired
    MockMvc mockMvc;

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

    SigninDTO signinDTO;
    TodoDTO todoDTO;

    TestLib testLib;

    final static String TOKEN_HEADER = "X-AUTH-TOKEN";

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

    @BeforeEach
    public void setTestDTO() {
        signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);

        todoDTO = new TodoDTO();
        todoDTO.setTitle(TestLib.testTodo.title);
        todoDTO.setContext(TestLib.testTodo.context);
    }

    public String getJwt() throws Exception {
        String jwt = mockMvc.perform(post(UrlMapper.Auth.signin)
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(); 
        log.info("jwt : " + jwt);
        return jwt;
    }
}