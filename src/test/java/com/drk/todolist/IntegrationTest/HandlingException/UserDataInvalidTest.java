package com.drk.todolist.Controller.HandlingException;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class UserDataInvalidTest extends IntegrationTest{
    
    public static final int FORBIDDEN = 403;

    public final UserDataInvalidException userDataInvalidException = new UserDataInvalidException();
    private String errorMessage;

    @BeforeEach
    public void errorMessageInit(){
        this.errorMessage = this.userDataInvalidException.getErrorMessage();
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
    @Transactional
    public void deleteTodoTest() throws Exception {
        UserEntity todoOwnerUserEntity = this.makeTestUser();
        TodoEntity  testTodoEntity = this.makeTodo(todoOwnerUserEntity);

        SignupDTO testUserSignupDTO = new SignupDTO();
        testUserSignupDTO.setUserName(TestLib.newTestUser.name);
        testUserSignupDTO.setNickName(TestLib.newTestUser.nickName);
        testUserSignupDTO.setPassword(TestLib.newTestUser.password);
        this.userService.signup(testUserSignupDTO);

        SigninDTO testUserSigninDTO = new SigninDTO();
        testUserSigninDTO.setUserName(TestLib.newTestUser.name);
        testUserSigninDTO.setPassword(TestLib.newTestUser.password);
        String testUserJwt = this.getJwt(testUserSigninDTO);

        this.mockMvc.perform(get(UrlMapper.Todo.deleteTodo)
            .header(TOKEN_HEADER, testUserJwt)
            .param("todoIdx", testTodoEntity.getIdx().toString()))
        .andDo(print())
        .andExpect(status().is(FORBIDDEN))
        .andExpect(content().string(this.errorMessage));
        }

    @Test
    @Transactional
    public void updateTodoTest() throws Exception {
        UserEntity todoOwnerUserEntity = this.makeTestUser();
        TodoEntity  testTodoEntity = this.makeTodo(todoOwnerUserEntity);

        SignupDTO testUserSignupDTO = new SignupDTO();
        testUserSignupDTO.setUserName(TestLib.newTestUser.name);
        testUserSignupDTO.setNickName(TestLib.newTestUser.nickName);
        testUserSignupDTO.setPassword(TestLib.newTestUser.password);
        this.userService.signup(testUserSignupDTO);

        SigninDTO testUserSigninDTO = new SigninDTO();
        testUserSigninDTO.setUserName(TestLib.newTestUser.name);
        testUserSigninDTO.setPassword(TestLib.newTestUser.password);
        String testUserJwt = this.getJwt(testUserSigninDTO);

        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setIdx(testTodoEntity.getIdx());
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);

        this.mockMvc.perform(post(UrlMapper.Todo.updateTodo)
            .header(TOKEN_HEADER, testUserJwt)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is(FORBIDDEN))
        .andExpect(content().string(this.errorMessage));
    }
}