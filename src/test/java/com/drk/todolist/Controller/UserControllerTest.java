package com.drk.todolist.Controller;

import com.drk.todolist.DTO.User.UserInfoDTO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest extends ControllerTestConfigure {

    @Test
    public void signup() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(testUserName);
        userInfoDTO.setNickName(testUserNickName);
        userInfoDTO.setPassword(testUserPassword);
        mockMvc.perform(post("/auth/signup")
            .content(asJsonString(userInfoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        assertEquals(userRepository.findByUsername(testUserName).getNickname(), testUserNickName);
    }

    @Test
    public void signin() throws Exception {
        makeTestUser();
        
        MvcResult result = mockMvc.perform(post("/auth/signin")
            .content(asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    
        String signinToken = result.getResponse().getContentAsString();
        log.info(signinToken);
        assertNotNull(signinToken);
    }

    @Test
    public void getUserInfo() throws Exception {
        makeTestUser();

        String jwt = getJwt();

        String userInfoPageBody = mockMvc.perform(get("/user/getUserInfo")
            .header("X-AUTH-TOKEN", jwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
        JSONObject userInfoJSONParsing = ((JSONObject) new JSONParser().parse(userInfoPageBody));
        assertEquals(userInfoJSONParsing.get("userName"), testUserName);
        assertEquals(userInfoJSONParsing.get("nickName"), testUserNickName);
        }

    @Test
    public void updateUserInfo() throws Exception {
        final String newUserName = "new user";
        final String newUserNickName = "new nick name";

        makeTestUser();
        String jwt = getJwt();

        UserInfoDTO newUserInfoDTO = new UserInfoDTO();
        newUserInfoDTO.setUserName(newUserName);
        newUserInfoDTO.setNickName(newUserNickName);

        String newUserJwt = mockMvc.perform(post("/user/updateUserInfo")
            .header("X-AUTH-TOKEN", jwt)
            .content(asJsonString(newUserInfoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

        String newUserInfoJson = mockMvc.perform(get("/user/getUserInfo")
            .header("X-AUTH-TOKEN", newUserJwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JSONObject userInfoJSONParsing = ((JSONObject) new JSONParser().parse(newUserInfoJson));
        assertEquals(userInfoJSONParsing.get("userName"), newUserName);
        assertEquals(userInfoJSONParsing.get("nickName"), newUserNickName);

        String newUserNickNameByFindRepo = userRepository.findByUsername(newUserName).getNickname();        
        assertEquals(newUserNickNameByFindRepo, newUserNickName);
    }

    @Test
    public void deleteUser() throws Exception {
        makeTestUser();
        String jwt = getJwt();

        String withdrawPageBody = mockMvc.perform(post("/user/withdraw")
            .header("X-AUTH-TOKEN", jwt)
            .param("password", testUserPassword))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        assertEquals(withdrawPageBody, "true");
        assertNull(userRepository.findByUsername(testUserName));
    }
}