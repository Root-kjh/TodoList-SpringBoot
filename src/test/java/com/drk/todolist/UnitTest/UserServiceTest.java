package com.drk.todolist.UnitTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.drk.todolist.Config.UnitTest;
import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserServiceTest extends UnitTest {


    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Test
    public void signup() throws Exception {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);

        this.userService.signup(signupDTO);
        UserEntity testUserEntity = this.userRepository.findByUsername(TestLib.testUser.name);

        assertEquals(testUserEntity.getUsername(), TestLib.testUser.name);
        assertEquals(testUserEntity.getNickname(), TestLib.testUser.nickName);
    }

    @Test
    public void signin() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);
        
        UserInfoDTO signUserInfoDTO = this.userService.signin(signinDTO);
        Authentication authentication = jwtTokenProvider.getAuthentication(signUserInfoDTO.getJwt());
        assertEquals(signUserInfoDTO.getUserId(), testUserEntity.getIdx());
        assertEquals(((UserEntity) authentication.getPrincipal()).getUsername(), TestLib.testUser.name);
    }

    @Test
    public void userinfoUpdate() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNickName(TestLib.newTestUser.nickName);

        UserInfoDTO updatUserInfoDTO = this.userService.userUpdate(testUserEntity, updateUserDTO);
        String updatedUserNickName = this.userRepository.findByUsername(testUserEntity.getUsername()).getNickname();

        assertEquals(updatUserInfoDTO.getNickName(), TestLib.newTestUser.nickName);
        assertEquals(updatedUserNickName, TestLib.newTestUser.nickName);
    }

    @Test
    public void userinfoDelete() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();

        this.userService.userDelete(testUserEntity);
        final boolean isUserExist = this.userRepository.findById(testUserEntity.getIdx()).isPresent();
        assertFalse(isUserExist);
    }

    @Test
    public void modifyPassword() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        this.userService.modifyPassowrd(testUserEntity, TestLib.newTestUser.password);
        
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.newTestUser.password);

        assertDoesNotThrow(()->this.userService.signin(signinDTO));
    }
}