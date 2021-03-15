package com.drk.todolist.Config;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;

import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class IntegrationTest extends TestInit{

    public MockMvc mockMvc;

    public UserService userService;

    public TodoService todoService;
    
    public JwtTokenProvider jwtTokenProvider;

    public final JSONParser jsonParser = new JSONParser();

    public final String TOKEN_HEADER= "X-AUTH-TOKEN";

    public final int BAD_REQUEST = 400;
    public final int FORBIDDEN = 403;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @BeforeEach
    public void setTestDTO() {
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);
        
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(TestLib.testTodo.title);
        todoDTO.setContext(TestLib.testTodo.context);
    }

    public String getJwt(){
        return jwtTokenProvider.coreateToken(TestLib.testUser.name);
    }

    public String getJwt(String userName){
        return jwtTokenProvider.coreateToken(userName);
    }
}