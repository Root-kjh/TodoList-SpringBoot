package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private UserSessionEntity userSessionEntity;

    private MockHttpSession session = new MockHttpSession();

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    @BeforeEach
    private void clearDBAndSession(){
        DBClear();
        sessionClear();
    }

    private void DBClear(){
        try{
            userRepository.deleteAll();
        } catch (Exception e) {}
    }

    private void sessionClear() {
        this.session = new MockHttpSession();
    }

    public void signupTestUser() throws Exception {
        System.out.println(userService);
        userService.signup(testUserName, testUserPassword, testUserNickName);
    }

    @Test
    public void signupTest() throws Exception{
        signupTestUser();
        final boolean isSignupUser = userRepository.isExistUser(testUserName);
        assertTrue(isSignupUser);
    }

    @Test
    public void signinTest() throws Exception{
        signupTestUser();
        if (userService.signin(this.session, testUserName, testUserPassword)){
            userSessionEntity = (UserSessionEntity) this.session.getAttribute("user");
            assertEquals(userSessionEntity.getUserNickName(), testUserNickName);       
        }else{
            throw new Exception("signin fail");
        }
        this.session = new MockHttpSession();
        final String wrong_user_name = "wrong_user_name";
        if (userService.signin(this.session, wrong_user_name, testUserPassword))
            throw new Exception("wrong userName signin success");
    }

    @Test
    public void logoutTest() throws Exception{
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        userService.logout(session);
        userSessionEntity = (UserSessionEntity) session.getAttribute("user");
        System.out.println(userSessionEntity);
    }

    @Test
    public void userinfoUpdateTest() throws Exception{
        final String newNickName = "newNickName";
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        if (userService.userinfoUpdate(session, null, newNickName, null, testUserPassword)){
            userSessionEntity = (UserSessionEntity) session.getAttribute("user");
            final String updatedUserName = userRepository.findById(userSessionEntity.getUserIdx()).get().getUsername();
            final String sessionNickName = userSessionEntity.getUserNickName();
            final String updatedNickName = userRepository.findById(userSessionEntity.getUserIdx()).get().getNickname();
            assertEquals(sessionNickName,newNickName); 
            assertEquals(updatedUserName,testUserName);
            assertEquals(updatedNickName,newNickName);            
        }
    }

    @Test
    public void userinfoDeleteTest() throws Exception{
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        final Long userIdx = ((UserSessionEntity) session.getAttribute("user")).getUserIdx();
        if (userService.userinfoDelete(session, testUserPassword)){
            assertNull(session.getAttribute("user"));
            assertFalse(userRepository.findById(userIdx).isPresent());
        }
    }
}