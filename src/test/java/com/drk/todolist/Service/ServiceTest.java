package com.drk.todolist.Service;

import com.drk.todolist.Entitis.UserSessionEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

public class ServiceTest {

    final public String testUserName = "test_user";
    final public String testUserNickName = "test_nick_name";
    final public String testUserPassword = "test_pw";

    final public String testTodoTitle = "testTodoTitle"; 
    final public String testTodoContext = "testTodoContext";

    public MockHttpSession session = new MockHttpSession();
    public UserSessionEntity userSessionEntity;

    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TodoService todoService;

    @Autowired
    public TodoRepository todoRepository;

    @AfterEach
    @BeforeEach
    public void clearDBAndSession(){
        DBClear();
        sessionClear();
    }

    private void DBClear(){
        try{
            userRepository.deleteAll();
            todoRepository.deleteAll();
        } catch (Exception e) {}
    }

    private void sessionClear() {
        this.session = new MockHttpSession();
    }

    public void signupTestUser() throws Exception {
        userService.signup(testUserName, testUserPassword, testUserNickName);
    }

    public void insertTodo() throws Exception{
        signupTestUser();
        Long userIdx = userRepository.findByUsernameAndPassword(testUserName, testUserPassword).getIdx();
        System.out.println(userIdx);
        todoService.insertTodo(userIdx, testTodoTitle, testTodoContext);
    }

}