package com.drk.todolist.Config;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ControllerTest extends TestInit{

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserService userService;

    @Autowired
    protected TodoService todoService;

    protected final static String TOKEN_HEADER = "X-AUTH-TOKEN";

    @BeforeEach
    public void setTestDTO() {
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);
        
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(TestLib.testTodo.title);
        todoDTO.setContext(TestLib.testTodo.context);
    }

    public String getJwt() throws Exception {

        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);

        String jwt = this.mockMvc.perform(post(UrlMapper.Auth.signin)
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(); 
        log.info("jwt : " + jwt);

        return jwt;
    }

    public String getJwt(SigninDTO signinDTO) throws Exception {
        String jwt = this.mockMvc.perform(post(UrlMapper.Auth.signin)
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