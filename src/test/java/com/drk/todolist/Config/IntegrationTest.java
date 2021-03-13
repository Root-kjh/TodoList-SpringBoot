package com.drk.todolist.Config;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;

import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;

import lombok.Builder;

public class IntegrationTest extends TestInit{

    protected final MockMvc mockMvc;

    protected final UserService userService;

    protected final TodoService todoService;
    
    protected final JwtTokenProvider jwtTokenProvider;

    protected final JSONParser jsonParser = new JSONParser();

    protected final static String TOKEN_HEADER= "X-AUTH-TOKEN";

    @Builder
    public IntegrationTest(
        UserRepository userRepository, 
        TodoRepository todoRepository, 
        MockMvc mockmMvc, 
        UserService userService, 
        TodoService todoService,
        JwtTokenProvider jwtTokenProvider) {
        super(userRepository, todoRepository);
        this.mockMvc = mockmMvc;
        this.userService = userService;
        this.todoService = todoService;
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