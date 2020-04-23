package com.drk.todolist.Controller;

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

    private String getAuthBasedControllerUrl(String url){
        return UrlMapper.Auth.baseUrl+url;
    }

    private String getUserBasedControllerUrl(String url){
        return UrlMapper.User.baseUrl+url;
    }

    @Test
    @Transactional
    public void signup() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(TestLib.testUser.name);
        userDTO.setNickName(TestLib.testUser.nickName);
        userDTO.setPassword(TestLib.testUser.password);
        mockMvc.perform(post(getAuthBasedControllerUrl(UrlMapper.Auth.signup))
            .content(TestLib.asJsonString(userDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        // signup user in Database
        testUserEntity = userRepository.findByUsername(TestLib.testUser.name);

        log.info("user DTO");
        log.info(userDTO.toString());
        log.info("signup User Entity");
        log.info(testUserEntity.toString());
        assertTrue(testLib.compareUserEntity(testUserEntity, userDTO));
    }

    @Test
    public void signin() throws Exception {
        testUserEntity = testLib.makeTestUser();
        
        String jwt = mockMvc.perform(post(getAuthBasedControllerUrl(UrlMapper.Auth.signin))
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

        String userInfoPageBody = mockMvc.perform(get(getUserBasedControllerUrl(UrlMapper.User.getUserInfo))
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
    public void updateUserInfo() throws Exception {
        testUserEntity = testLib.makeTestUser();
        String jwt = getJwt();

        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUserName(TestLib.newTestUser.name);
        newUserDTO.setNickName(TestLib.newTestUser.nickName);

        String newUserJwt = 
            mockMvc.perform(post(getUserBasedControllerUrl(UrlMapper.User.updateUserInfo))
                .header(TOKEN_HEADER, jwt)
                .content(TestLib.asJsonString(newUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        log.info("new User JWT : "+newUserJwt);

        String newUserInfoJson = 
            mockMvc.perform(get(getUserBasedControllerUrl(UrlMapper.User.getUserInfo))
                .header(TOKEN_HEADER, newUserJwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JSONObject userInfoJSONParsing = ((JSONObject) new JSONParser().parse(newUserInfoJson));
        assertEquals(userInfoJSONParsing.get("userName"), newUserDTO.getUserName());
        assertEquals(userInfoJSONParsing.get("nickName"), newUserDTO.getNickName());
    }

    @Test
    public void deleteUser() throws Exception {
        testUserEntity = testLib.makeTestUser();
        String jwt = getJwt();
        mockMvc.perform(post(getUserBasedControllerUrl(UrlMapper.User.withdraw))
            .header(TOKEN_HEADER, jwt)
            .param("password", TestLib.testUser.password))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        assertNull(userRepository.findByUsername(TestLib.testUser.name));
    }
}