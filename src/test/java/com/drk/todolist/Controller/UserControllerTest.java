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
import org.springframework.test.web.servlet.MvcResult;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        String userInfoPageBody = mockMvc.perform(get(UrlMapper.User.getUserInfo)
            .header(TOKEN_HEADER, jwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
        JSONObject userInfoJSONParsing = ((JSONObject) new JSONParser().parse(userInfoPageBody));
  
        // JSONObject UserENtity로 변환 후 Compare
        // UserDTO userDTO = new UserDTO();
        // userDTO.setNickName((String) userInfoJSONParsing.get("nickName"));
        // userDTO.setUserName((String) userInfoJSONParsing.get("userName"));
        // userDTO.setPassword((String) userInfoJSONParsing.get("password"));
        
        // assertTrue(testLib.compareUserEntity(dbUserEntity, localUserEntity));
        }

    @Test
    public void updateUserInfo() throws Exception {
        final String newUserName = "new user";
        final String newUserNickName = "new nick name";

        makeTestUser();
        String jwt = getJwt();

        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUserName(newUserName);
        newUserDTO.setNickName(newUserNickName);

        String newUserJwt = mockMvc.perform(post("/user/updateUserInfo")
            .header("X-AUTH-TOKEN", jwt)
            .content(asJsonString(newUserDTO))
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