package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.drk.todolist.Entitis.LoginedUserSessionEntity;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.User.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;


@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class User {

    final private String test_user_name="test_user";
    final private String test_user_nick_name="test_nick_name";
    final private String test_user_password="test_pw";

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    @BeforeEach
    private void DBClear(){
        try{
            userRepository.deleteByUser_name(test_user_name);
        }catch (Exception e){}
    } 

    @AfterEach
    @BeforeEach
    private void sessionClear(){
        session = new MockHttpSession();
    }

    public void signupTestUser() throws Exception{
        System.out.println(userService);
        userService.signup(test_user_name, test_user_password, test_user_nick_name);
    }

    @Test
    public void signupTest(){
        try{
            signupTestUser();
            final boolean isSignupUser = userRepository.isExistUser(test_user_name);
            assertEquals(isSignupUser, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void signinTest(){
        try{
            signupTestUser();
            userService.signin(session, test_user_name, test_user_password);
            LoginedUserSessionEntity sessionEntity = (LoginedUserSessionEntity) session.getAttribute("user");
            assertEquals(sessionEntity.getUser_nick_name(), test_user_nick_name);
        
            session = new MockHttpSession();
            final String wrong_user_name = "wrong_user_name";
            userService.signin(session, wrong_user_name, test_user_password);
            sessionEntity = (LoginedUserSessionEntity) session.getAttribute("user");
            assertNotEquals(sessionEntity.getUser_nick_name(), test_user_nick_name);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void logoutTest(){
        try{
            signupTestUser();
            userService.signin(session, test_user_name, test_user_password);
            userService.logout(session);
            LoginedUserSessionEntity sessionEntity = (LoginedUserSessionEntity) session.getAttribute("user");
            System.out.println(sessionEntity);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}