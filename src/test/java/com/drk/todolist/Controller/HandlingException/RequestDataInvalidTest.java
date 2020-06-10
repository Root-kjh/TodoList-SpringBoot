package com.drk.todolist.Controller.HandlingException;

import com.drk.todolist.Config.ControllerTest;
import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.lib.TestLib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestDataInvalidTest extends ControllerTest{
    
    final static int METHOD_NOT_ALLOWED = 405;

    RequestDataInvalidException requestDataInvalidException = new RequestDataInvalidException();

    @Test
    public void signupTest() throws Exception {
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);

        this.mockMvc.perform(post(UrlMapper.Auth.signup)
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(METHOD_NOT_ALLOWED))
        .andExpect(content().string(this.requestDataInvalidException.getErrorMessage()));

        assertEquals(this.userRepository.count(), 0);
    }

    @Test
    public void signinTest() throws Exception {
        InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
        insertTodoDTO.setTitle(TestLib.testTodo.title);
        insertTodoDTO.setContext(TestLib.testTodo.context);

        this.mockMvc.perform(post(UrlMapper.Auth.signin)
            .content(TestLib.asJsonString(insertTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(METHOD_NOT_ALLOWED))
        .andExpect(content().string(this.requestDataInvalidException.getErrorMessage()));
    }

    @Test
    public void updateUserInfoTest() throws Exception {
        this.makeTestUser();
        String jwt = this.getJwt();

        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);

        this.mockMvc.perform(post(UrlMapper.User.updateUserInfo)
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(METHOD_NOT_ALLOWED))
        .andExpect(content().string(this.requestDataInvalidException.getErrorMessage()));
    }

    @Test
    @Transactional
    public void insertTodoTest() throws Exception {
        this.makeTestUser();
        String jwt = this.getJwt();

        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setIdx(0L);
        updateTodoDTO.setNewTitle(TestLib.testTodo.title);
        updateTodoDTO.setNewContext(TestLib.testTodo.context);

        this.mockMvc.perform(post(UrlMapper.Todo.insertTodo)
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(METHOD_NOT_ALLOWED))
        .andExpect(content().string(this.requestDataInvalidException.getErrorMessage()));
    }

    @Test
    @Transactional
    public void updateTodoTest() throws Exception {
        this.makeTestUser();
        String jwt = this.getJwt();

        InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
        insertTodoDTO.setTitle(TestLib.testTodo.title);
        insertTodoDTO.setContext(TestLib.testTodo.context);

        this.mockMvc.perform(post(UrlMapper.Todo.updateTodo)
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(insertTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(METHOD_NOT_ALLOWED))
        .andExpect(content().string(this.requestDataInvalidException.getErrorMessage()));
    }
}