package com.drk.todolist.IntegrationTest.Basic;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest extends IntegrationTest {

    String jwt;
    UserEntity testUserEntity;

    @BeforeEach
    public void setTestUser() throws Exception{
        this.testUserEntity = this.makeTestUser();
        this.jwt = this.getJwt();
    }

    @Test
    public void insertTodo() throws Exception {
        TodoDTO newTodoDTO = new TodoDTO();
        newTodoDTO.setTitle(TestLib.testTodo.title);
        newTodoDTO.setContext(TestLib.testTodo.context);

        this.mockMvc.perform(post("/todo")
            .header(TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(newTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"))
        .andReturn().getResponse().getContentAsString();

        TodoEntity insertedTodo = this.todoRepository.findAll().get(0);

        assertEquals(insertedTodo.getTitle(), TestLib.testTodo.title);
        assertEquals(insertedTodo.getContext(), TestLib.testTodo.context);
    }

    @Test
    public void showTodo() throws Exception {
        final int todoCount = 5;

        
        for(int i=0;i<todoCount;i++){
            this.makeTodo(this.testUserEntity);
        }

        String response = 
        this.mockMvc.perform(get("/todo")
                .header(TOKEN_HEADER, this.jwt))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        JSONArray responseJson = (JSONArray) this.jsonParser.parse(response);
        
        for(int i=0;i<todoCount;i++){
            JSONObject todo = ((JSONObject)responseJson.get(i));
            assertEquals(todo.get("title"), TestLib.testTodo.title);
            assertEquals(todo.get("context"), TestLib.testTodo.context);
        }
    }

    @Test
    public void updateTodo() throws Exception {
        Long testTodoIdx = this.makeTodo(this.testUserEntity).getIdx();

        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);

        String response = this.mockMvc.perform(put("/todo/"+testTodoIdx)
            .header(TOKEN_HEADER, this.jwt)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        JSONObject responseJson = (JSONObject) this.jsonParser.parse(response);
        TodoEntity updatedTodo = this.todoRepository.findById(testTodoIdx).get();
        
        assertEquals(updatedTodo.getTitle(), TestLib.newTestTodo.title);
        assertEquals(updatedTodo.getContext(), TestLib.newTestTodo.context);
        assertEquals(responseJson.get("title"), TestLib.newTestTodo.title);
        assertEquals(responseJson.get("context"), TestLib.newTestTodo.context);
    }

    @Test
    public void deleteTodo() throws Exception {
        Long testTodoIdx = this.makeTodo(this.testUserEntity).getIdx();
        String response = this.mockMvc.perform(delete("/todo/"+testTodoIdx)
            .header(TOKEN_HEADER, this.jwt))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");

        assertEquals(responseMessage, "Success");
        assertFalse(this.todoRepository.findById(testTodoIdx).isPresent());
    }
}