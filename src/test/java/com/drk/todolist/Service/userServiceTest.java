package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.JWT.JwtService;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Slf4j
public class UserServiceTest extends ServiceTest {

    @Autowired
    JwtService jwtService;

    @Test
    @Transactional
    public void signup() throws Exception {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUserName(TestLib.testUser.name);
        newUserDTO.setNickName(TestLib.testUser.nickName);
        newUserDTO.setPassword(TestLib.testUser.password);
        this.userService.signup(newUserDTO);
        this.testUserEntity = this.userRepository.findByUsername(TestLib.testUser.name);
        log.info("Signup User DTO");
        log.info(newUserDTO.toString());
        log.info("Signup User Entity");
        log.info(this.testUserEntity.toString());
        assertTrue(this.testLib.compareUserEntity(this.testUserEntity, newUserDTO));
    }

    @Test
    @Transactional
    public void signin() throws Exception {
        final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtService);
        this.testUserEntity = this.testLib.makeTestUser();
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);
        log.info("signin DTO");
        log.info(signinDTO.toString());
        
        assertTrue(this.userService.isCanLogin(signinDTO));
        String token = jwtTokenProvider.coreateToken(TestLib.testUser.name);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        log.info("jwt userEntity");
        log.info(this.testUserEntity.toString());
        assertTrue(this.testLib.compareUserEntity(this.testUserEntity, 
            ((UserEntity) authentication.getPrincipal())));
    }
}