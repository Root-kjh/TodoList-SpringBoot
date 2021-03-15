package com.drk.todolist.IntegrationTest.HandlingException.TodoController;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestDataInvalidTest extends IntegrationTest{

    UserEntity testUserEntity;
    String jwt;

    @BeforeEach
    public void makeUser() throws Exception{
        this.testUserEntity = this.makeTestUser();
        this.jwt = this.getJwt();
    }

    @Test
    public void insertTodo() throws Exception{
        InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
        insertTodoDTO.setTitle(TestLib.testTodo.title);
        String response = this.mockMvc.perform(post("/todo")
            .header(this.TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(insertTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Request Data Invalid");
        assertEquals(this.todoRepository.count(), 0);
    }

    @Test
    public void updateTodo() throws Exception{
        Long todoIdx = this.makeTodo(this.testUserEntity).getIdx();
        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        String response = this.mockMvc.perform(put("/todo/"+todoIdx)
            .header(this.TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST))
        .andReturn().getResponse().getContentAsString();
        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");
        
        assertEquals(responseMessage, "Request Data Invalid");
        assertEquals(this.todoRepository.findAll().get(0).getTitle(), TestLib.testTodo.title);
    }
}
