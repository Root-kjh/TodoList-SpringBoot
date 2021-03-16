package com.drk.todolist.IntegrationTest.HandlingException.UserController;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.lib.TestLib;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserExistTest extends IntegrationTest{
    
    @Test
    public void signup() throws Exception{
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);
        this.userService.signup(signupDTO);

        String response = this.mockMvc.perform(post("/auth/signup")
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "User Exist");
    }
}
