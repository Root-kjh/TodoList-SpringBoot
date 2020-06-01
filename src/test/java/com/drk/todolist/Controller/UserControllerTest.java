package com.drk.todolist.Controller;

import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.lib.TestLib;
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
public class UserControllerTest extends ControllerTestConfigure {

    @Test
    @Transactional
    public void signup() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(TestLib.testUser.name);
        userDTO.setNickName(TestLib.testUser.nickName);
        userDTO.setPassword(TestLib.testUser.password);
        mockMvc.perform(post(UrlMapper.Auth.signup)
            .content(TestLib.asJsonString(userDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        // signup user in Database
        this.testUserEntity = this.userRepository.findByUsername(TestLib.testUser.name);

        log.info("user DTO");
        log.info(userDTO.toString());
        log.info("signup User Entity");
        log.info(this.testUserEntity.toString());
        assertTrue(this.testLib.compareUserEntity(this.testUserEntity, userDTO));
    }

    @Test
    public void signin() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        
        String jwt = mockMvc.perform(post(UrlMapper.Auth.signin)
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
    public void getUserInfo() throws Exception {
        testUserEntity = testLib.makeTestUser();

        String jwt = getJwt();

        String userInfoPageBody = mockMvc.perform(get(UrlMapper.User.getUserInfo)
                .header(TOKEN_HEADER, jwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
        JSONObject userInfoJson = ((JSONObject) new JSONParser().parse(userInfoPageBody));
        log.info(userInfoJson.toString());

        assertEquals(userInfoJson.get("userName"), this.testUserEntity.getUsername());
        assertEquals(userInfoJson.get("nickName"), this.testUserEntity.getNickname());
        }

    @Test
    public void updateUserInfo() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        String jwt = getJwt();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNewUserName(TestLib.newTestUser.name);
        updateUserDTO.setNewNickName(TestLib.newTestUser.nickName);

        String newUserJwt = 
            mockMvc.perform(post(UrlMapper.User.updateUserInfo)
                .header(TOKEN_HEADER, jwt)
                .content(TestLib.asJsonString(updateUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        log.info("new User JWT : "+newUserJwt);

        String newUserInfoJson = 
            mockMvc.perform(get(UrlMapper.User.getUserInfo)
                .header(TOKEN_HEADER, newUserJwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JSONObject userInfoJSONParsing = ((JSONObject) new JSONParser().parse(newUserInfoJson));
        assertEquals(userInfoJSONParsing.get("userName"), updateUserDTO.getNewUserName());
        assertEquals(userInfoJSONParsing.get("nickName"), updateUserDTO.getNewNickName());
    }

    @Test
    public void deleteUser() throws Exception {
        testUserEntity = testLib.makeTestUser();
        String jwt = getJwt();
        mockMvc.perform(post(UrlMapper.User.withdraw)
            .header(TOKEN_HEADER, jwt)
            .param("password", TestLib.testUser.password))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        assertNull(userRepository.findByUsername(TestLib.testUser.name));
    }
}