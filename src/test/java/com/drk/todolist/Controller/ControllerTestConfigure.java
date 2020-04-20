package com.drk.todolist.Controller;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.JWT.JwtService;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTestConfigure {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserService userService;

    @Autowired
    TodoService todoService;

    @Autowired
    JwtService jwtService;

    @BeforeEach
    @AfterEach
    public void cleanDB() {
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setTestDTO() {
        signinDTO.setUserName(testUserName);
        signinDTO.setPassword(testUserPassword);

        todoDTO.setTitle(testTodoTitle);
        todoDTO.setContext(testTodoContext);
    }

    public SigninDTO signinDTO = new SigninDTO();
    public TodoDTO todoDTO = new TodoDTO();

    final String testUserName = "test";
    final String testUserNickName = "testNickName";
    final String testUserPassword = "testpw";

    final String testTodoTitle = "test todo";
    final String testTodoContext = "test context";

    public String getJwt() throws Exception {
        return mockMvc.perform(post("/auth/signin")
            .content(asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(); 
    }

    public void makeTestUser(){
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(testUserName);
        userInfoDTO.setNickName(testUserNickName);
        userInfoDTO.setPassword(testUserPassword);
        userService.signup(userInfoDTO);
    }

    public void makeTestTodo(Long userIdx){
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(testTodoTitle);
        todoDTO.setContext(testTodoContext);
        todoService.insertTodo(userIdx, todoDTO);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}