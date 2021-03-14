package com.drk.todolist.Controller.HandlingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.Config.JWT.JwtTokenProvider;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends IntegrationTest{
    
    final static int BAD_REQUEST = 400;
    final static int FORBIDDEN = 403;

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
        .andExpect(content().string(this.errorMessage));

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
        .andExpect(content().string(this.errorMessage));
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
        .andExpect(content().string(this.errorMessage));
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
        .andExpect(content().string(this.errorMessage));
    }

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
        .andExpect(content().string(this.errorMessage));
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