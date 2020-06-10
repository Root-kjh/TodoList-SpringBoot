package com.drk.todolist.Controller.Basic;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;
import com.drk.todolist.Config.ControllerTest;
import com.drk.todolist.Config.Controller.UrlMapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest extends ControllerTest {

    @Test
    @Transactional
    public void signup() throws Exception {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);
        this.mockMvc.perform(post(UrlMapper.Auth.signup)
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        // signup user in Database
        UserEntity testUserEntity = 
            this.userRepository.findByUsername(TestLib.testUser.name);

        log.info("user DTO");
        log.info(signupDTO.toString());
        log.info("signup User Entity");
        log.info(testUserEntity.toString());
        assertTrue(TestLib.compareUserEntity(testUserEntity, signupDTO));
    }

    @Test
    @Transactional
    public void signin() throws Exception {
        this.makeTestUser();
        
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);

        String jwt = this.mockMvc.perform(post(UrlMapper.Auth.signin)
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andReturn().getResponse().getContentAsString();
    
        log.info("jwt Token");
        log.info(jwt);

        assertNotEquals(jwt, "false");
    }

    @Test
    @Transactional
    public void getUserInfo() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();

        String jwt = this.getJwt();

        String userInfoPageBody = this.mockMvc.perform(get(UrlMapper.User.getUserInfo)
                .header(TOKEN_HEADER, jwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
        JSONObject userInfoJson = ((JSONObject) new JSONParser().parse(userInfoPageBody));
        log.info(userInfoJson.toString());

        assertEquals(userInfoJson.get("userName"), testUserEntity.getUsername());
        assertEquals(userInfoJson.get("nickName"), testUserEntity.getNickname());
        }

    @Test
    @Transactional
    public void updateUserInfo() throws Exception {
        this.makeTestUser();
        String jwt = this.getJwt();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNewUserName(TestLib.newTestUser.name);
        updateUserDTO.setNewNickName(TestLib.newTestUser.nickName);

        String newUserJwt = 
            this.mockMvc.perform(post(UrlMapper.User.updateUserInfo)
                .header(TOKEN_HEADER, jwt)
                .content(TestLib.asJsonString(updateUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        log.info("new User JWT : "+newUserJwt);

        String newUserInfoJson = 
            this.mockMvc.perform(get(UrlMapper.User.getUserInfo)
                .header(TOKEN_HEADER, newUserJwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JSONObject userInfoJSONParsing = ((JSONObject) new JSONParser().parse(newUserInfoJson));
        
        assertEquals(userInfoJSONParsing.get("userName"), updateUserDTO.getNewUserName());
        assertEquals(userInfoJSONParsing.get("nickName"), updateUserDTO.getNewNickName());
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception {
        this.makeTestUser();
        String jwt = this.getJwt();
        mockMvc.perform(post(UrlMapper.User.withdraw)
            .header(TOKEN_HEADER, jwt)
            .param("password", TestLib.testUser.password))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        assertNull(this.userRepository.findByUsername(TestLib.testUser.name));
    }

    @Test
    @Transactional
    public void modifyPassword() throws Exception {
        this.makeTestUser();
        String jwt = this.getJwt();
        this.mockMvc.perform(post(UrlMapper.User.modifyPassword)
            .header(TOKEN_HEADER, jwt)
            .param("newPassword", TestLib.newTestUser.password))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.newTestUser.password);
        log.info("signinDTO");
        log.info(signinDTO.toString());

        assertTrue(this.userService.isCanLogin(signinDTO));
    }
}