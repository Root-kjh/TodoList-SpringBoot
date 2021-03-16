package com.drk.todolist.IntegrationTest.HandlingException.TodoController;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class PermissionDeniedTest extends IntegrationTest{
    
    UserEntity testUserEntity;
    UserEntity newUserEntity;
    String jwt;
    Long newUserTodoIdx;

    @BeforeEach
    public void setUsers() throws Exception{
        this.testUserEntity = this.makeTestUser();
        
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.newTestUser.name);
        signupDTO.setNickName(TestLib.newTestUser.nickName);
        signupDTO.setPassword(TestLib.newTestUser.password);
        this.jwt = this.getJwt();
        this.newUserEntity = this.makeTestUser(signupDTO);
        this.newUserTodoIdx = this.makeTodo(this.newUserEntity).getIdx();
    }

    @Test
    public void updateTodo() throws Exception{
        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);
        String response = this.mockMvc.perform(put("/todo/"+this.newUserTodoIdx)
            .header(this.TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Permission Denied");
        assertEquals(this.todoRepository.findAll().get(0).getTitle(), TestLib.testTodo.title);

    }

    @Test
    public void deleteTodo() throws Exception{
        String response = this.mockMvc.perform(delete("/todo/"+this.newUserTodoIdx)
            .header(this.TOKEN_HEADER, this.jwt))
        .andExpect(status().is(FORBIDDEN))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Permission Denied");
        assertTrue(this.todoRepository.findById(this.newUserTodoIdx).isPresent());

    }
}
