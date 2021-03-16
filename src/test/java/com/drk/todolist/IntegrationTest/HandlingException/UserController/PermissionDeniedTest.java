package com.drk.todolist.IntegrationTest.HandlingException.UserController;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

@SpringBootTest
@AutoConfigureMockMvc
public class PermissionDeniedTest extends IntegrationTest{
    
    UserEntity testUser;
    Long newUserIdx;
    String jwt;

    @BeforeEach
    public void setUsers() throws Exception{
        this.testUser = this.makeTestUser();
        
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.newTestUser.name);
        signupDTO.setNickName(TestLib.newTestUser.nickName);
        signupDTO.setPassword(TestLib.newTestUser.password);
        this.jwt = this.getJwt();
        this.newUserIdx = this.makeTestUser(signupDTO).getIdx();
    }

    @Test
    public void getUserInfo() throws Exception{
        String response = this.mockMvc.perform(get("/user/"+this.newUserIdx)
        .header(TOKEN_HEADER, this.jwt))
        .andExpect(status().is(FORBIDDEN))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Permission Denied");
    }
    
    @Test
    public void updateUserInfo() throws Exception{
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNickName(TestLib.testUser.nickName);
        String response = this.mockMvc.perform(put("/user/"+this.newUserIdx)
            .header(TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(updateUserDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Permission Denied");
        assertEquals(
            this.userRepository.findById(this.newUserIdx).get().getNickname(), 
            TestLib.newTestUser.nickName
        );
    }
    
    @Test
    public void modifyPassword() throws Exception{
        String response = this.mockMvc.perform(patch("/user/"+this.newUserIdx)
            .header(TOKEN_HEADER, this.jwt)
            .content("{\"password\": \""+TestLib.newTestUser.password+"\"}")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);

        assertEquals(responseMessage, "Permission Denied");
        assertDoesNotThrow(()->this.userService.signin(signinDTO));
    }
    
    @Test
    public void withdraw() throws Exception{
        String response = this.mockMvc.perform(delete("/user/"+this.newUserIdx)
            .header(TOKEN_HEADER, this.jwt))
        .andExpect(status().is(FORBIDDEN))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Permission Denied");
        assertEquals(this.userRepository.count(), 2);
    }
}
