package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.DTO.UserSessionDTO;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;




@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class User extends ServiceTest{

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
            userSessionDTO = (UserSessionDTO) this.session.getAttribute("user");
            assertEquals(userSessionDTO.getUserNickName(), testUserNickName);       
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
        userSessionDTO = (UserSessionDTO) session.getAttribute("user");
        System.out.println(userSessionDTO);
    }

    @Test
    public void userinfoUpdateTest() throws Exception{
        final String newNickName = "newNickName";
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        if (userService.userinfoUpdate(session, null, newNickName, null, testUserPassword)){
            userSessionDTO = (UserSessionDTO) session.getAttribute("user");
            final String updatedUserName = userRepository.findById(userSessionDTO.getUserIdx()).get().getUsername();
            final String sessionNickName = userSessionDTO.getUserNickName();
            final String updatedNickName = userRepository.findById(userSessionDTO.getUserIdx()).get().getNickname();
            assertEquals(sessionNickName,newNickName); 
            assertEquals(updatedUserName,testUserName);
            assertEquals(updatedNickName,newNickName);            
        }
    }

    @Test
    public void userinfoDeleteTest() throws Exception{
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        final Long userIdx = ((UserSessionDTO) session.getAttribute("user")).getUserIdx();
        if (userService.userinfoDelete(session, testUserPassword)){
            assertNull(session.getAttribute("user"));
            assertFalse(userRepository.findById(userIdx).isPresent());
        }
    }
}