package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.drk.todolist.DTO.User.UserInfoDTO;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class userServiceTest extends ServiceTest {
    
    @Test
    public void signup() {
        UserInfoDTO newUser = new UserInfoDTO();
        newUser.setNickName(testUserNickName);
        newUser.setUserName(testUserName);
        newUser.setPassword(testUserPassword);
        userService.signup(newUser);
        assertEquals(userRepository.findByUsername(testUserName).getUsername(),testUserName);
    }

}