package com.drk.todolist.IntegrationTest.HandlingException.UserController;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

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

@SpringBootTest
@AutoConfigureMockMvc
public class UnAuthTest extends IntegrationTest{
    
    UserEntity testUser;

    @BeforeEach
    public void setUser() throws Exception{
        this.testUser = this.makeTestUser();
    }

    @Test
    public void getUserInfo() throws Exception{
        this.mockMvc.perform(get("/user/"+this.testUser.getIdx()))
        .andExpect(status().is(FORBIDDEN));
    }

    @Test
    public void updateUserInfo() throws Exception{
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNickName(TestLib.newTestUser.nickName);

        this.mockMvc.perform(put("/user/"+this.testUser.getIdx())
            .content(TestLib.asJsonString(updateUserDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN));

        assertEquals(
            this.userRepository.findAll().get(0).getNickname(), 
            TestLib.testUser.nickName
        );
    }

    @Test
    public void modifyPassword() throws Exception{
        this.mockMvc.perform(patch("/user/"+this.testUser.getIdx())
            .content("{\"password\": \""+TestLib.newTestUser.password+"\"}")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN));

        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);
        assertDoesNotThrow(()->this.userService.signin(signinDTO));
    }

    @Test
    public void withdraw() throws Exception{
        this.mockMvc.perform(delete("/user/"+this.testUser.getIdx()))
        .andExpect(status().is(FORBIDDEN));

        assertEquals(this.userRepository.count(), 1);
    }
}
