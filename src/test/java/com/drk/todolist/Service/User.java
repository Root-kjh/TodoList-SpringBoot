package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.drk.todolist.Entitis.UserSessionEntity;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.User.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;


@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class User {

    final private String testUserName="test_user";
    final private String testUserNickName="test_nick_name";
    final private String testUserPassword="test_pw";

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    @BeforeEach
    private void DBClear(){
        try{
            userRepository.deleteByUsername(testUserName);
        } catch (Exception e) {
        }
    }

    @AfterEach
    @BeforeEach
    private void sessionClear() {
        session = new MockHttpSession();
    }

    public void signupTestUser() throws Exception {
        System.out.println(userService);
        userService.signup(testUserName, testUserPassword, testUserNickName);
    }

    @Test
    public void signupTest() throws Exception{
        signupTestUser();
        final boolean isSignupUser = userRepository.isExistUser(testUserName);
        assertEquals(isSignupUser, true);
    }

    @Test
    public void signinTest() throws Exception{
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        UserSessionEntity userSessionEntity = (UserSessionEntity) session.getAttribute("user");
        assertEquals(userSessionEntity.getUserNickName(), testUserNickName);

        session = new MockHttpSession();
        final String wrong_user_name = "wrong_user_name";
        userService.signin(session, wrong_user_name, testUserPassword);
        userSessionEntity = (UserSessionEntity) session.getAttribute("user");
        assertNotEquals(userSessionEntity.getUserNickName(), testUserNickName);
    }

    @Test
    public void logoutTest() throws Exception{
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        userService.logout(session);
        UserSessionEntity userSessionEntity = (UserSessionEntity) session.getAttribute("user");
        System.out.println(userSessionEntity);
    }
}