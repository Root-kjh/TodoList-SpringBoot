package com.drk.todolist.Controller.HandlingException;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.drk.todolist.Config.ControllerTest;
import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserDataInvalidTest extends ControllerTest{
    
    public static final int FORBIDDEN = 403;

    public final UserDataInvalidException userDataInvalidException = new UserDataInvalidException();

    @Test
    public void signinTest() throws Exception {
        this.makeTestUser();
        SigninDTO signinDTO = new SigninDTO();

        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword("false password");

        this.mockMvc.perform(post(UrlMapper.Auth.signin)
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(FORBIDDEN))
        .andExpect(content().string(this.userDataInvalidException.getErrorMessage()));
    }
}