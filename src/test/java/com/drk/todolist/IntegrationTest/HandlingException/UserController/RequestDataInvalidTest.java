package com.drk.todolist.IntegrationTest.HandlingException.UserController;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestDataInvalidTest extends IntegrationTest{
    
    UserEntity testUser;
    String jwt;

    @BeforeEach
    public void setUser() throws Exception{
        this.testUser = this.makeTestUser();
        this.jwt = this.getJwt();
    }

    @Test
    public void signup() throws Exception{
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.newTestUser.name);
        signupDTO.setNickName(TestLib.newTestUser.nickName);

        String response = this.mockMvc.perform(post("/auth/signup")
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Request Data Invalid");
        assertEquals(this.userRepository.count(), 1);
    }

    @Test
    public void signin() throws Exception{
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setPassword(TestLib.testUser.password);

        String response = this.mockMvc.perform(post("/auth/signin")
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Request Data Invalid");
    }

    @Test
    public void updateUserInfo() throws Exception{
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        String response = this.mockMvc.perform(put("/user/"+this.testUser.getIdx())
            .header(this.TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(updateUserDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Request Data Invalid");
    }

    @Test
    public void modifyPassword() throws Exception{
        String response = this.mockMvc.perform(patch("/user/"+this.testUser.getIdx())
            .header(TOKEN_HEADER, jwt)
            .content("{\"pw\": \""+TestLib.newTestUser.password+"\"}")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
    
        assertEquals(responseMessage, "Request Data Invalid");
    }
}
