package com.drk.todolist.Controller.HandlingException;

import com.drk.todolist.Config.ControllerTest;
import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.UserExistException;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserExistTest extends ControllerTest{
    
    private UserExistException userExistException = new UserExistException();
    private static final int NOT_ACCEPTABLE = 406;
    private String errorMessage;

    @BeforeEach
    public void setErrorMessage(){
        errorMessage = this.userExistException.getErrorMessage();
    }

    @Test
    public void signupTest() throws Exception {
        this. makeTestUser();
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);

        this.mockMvc.perform(post(UrlMapper.Auth.signup)
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(NOT_ACCEPTABLE))
        .andExpect(content().string(this.errorMessage));

        assertEquals(this.userRepository.count(), 1);
    }

    @Test
    public void updateUserInfoTest() throws Exception {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.newTestUser.name);
        signupDTO.setNickName(TestLib.newTestUser.nickName);
        signupDTO.setPassword(TestLib.newTestUser.password);

        this.mockMvc.perform(post(UrlMapper.Auth.signup)
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        this.makeTestUser();
        String jwt = this.getJwt();


        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNewUserName(TestLib.newTestUser.name);
        updateUserDTO.setNewNickName(TestLib.newTestUser.nickName);

        this.mockMvc.perform(post(UrlMapper.User.updateUserInfo)
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(updateUserDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(NOT_ACCEPTABLE))
        .andExpect(content().string(this.errorMessage));
    }
}