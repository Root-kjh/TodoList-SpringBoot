package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.JWT.JwtService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserServiceTest extends ServiceTest {

    @Autowired
    JwtService jwtService;

    @Test
    public void signup() throws Exception {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setNickName(testUserNickName);
        newUserDTO.setUserName(testUserName);
        newUserDTO.setPassword(testUserPassword);
        userService.signup(newUserDTO);
        assertEquals(userRepository.findByUsername(testUserName).getUsername(),testUserName);
    }

    @Test
    @Transactional
    public void signin() throws Exception {
        final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtService);
        signup();
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(testUserName);
        signinDTO.setPassword(testUserPassword);
        assertTrue(userService.isCanLogin(signinDTO));
        String token = jwtTokenProvider.coreateToken(testUserName);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        assertEquals(((UserEntity) authentication.getPrincipal()).getNickname(), testUserNickName);
    }
}