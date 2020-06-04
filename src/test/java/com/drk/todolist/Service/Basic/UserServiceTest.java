package com.drk.todolist.Service.Basic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Service.ServiceTestConfigure;
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
public class UserServiceTest extends ServiceTestConfigure {

    @Autowired
    JwtService jwtService;

    @Test
    @Transactional
    public void signup() throws Exception {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);
        log.info("Signup User DTO");
        log.info(signupDTO.toString());

        this.userService.signup(signupDTO);
        this.testUserEntity = this.userRepository.findByUsername(TestLib.testUser.name);
        log.info("Signup User Entity");
        log.info(this.testUserEntity.toString());

        assertTrue(this.testLib.compareUserEntity(this.testUserEntity, signupDTO));
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

    @Test
    @Transactional
    public void userinfoUpdate() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        log.info("testUser");
        log.info(testUserEntity.toString());

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNewUserName(TestLib.newTestUser.name);
        updateUserDTO.setNewNickName(TestLib.newTestUser.nickName);
        log.info("updateUserDTO");
        log.info(updateUserDTO.toString());
        
        this.userService.userinfoUpdate(testUserEntity, updateUserDTO);
        this.testUserEntity = this.userRepository.getOne(this.testUserEntity.getIdx());

        assertTrue(this.testLib.compareUserEntity(this.testUserEntity, updateUserDTO));
    }

    @Test
    @Transactional
    public void userinfoDelete() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        log.info("testUser");
        log.info(this.testUserEntity.toString());

        this.userService.userinfoDelete(this.testUserEntity, TestLib.testUser.password);
        final boolean isUserExist = this.userRepository.findById(this.testUserEntity.getIdx()).isPresent();
        assertFalse(isUserExist);
    }

    @Test
    @Transactional
    public void modifyPassword() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        log.info("testUser");
        log.info(this.testUserEntity.toString());

        this.userService.modifyPassowrd(this.testUserEntity, TestLib.newTestUser.password);
        
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.newTestUser.password);
        log.info("signinDTO");
        log.info(signinDTO.toString());

        assertTrue(this.userService.isCanLogin(signinDTO));
    }
}