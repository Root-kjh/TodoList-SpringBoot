package com.drk.todolist.IntegrationTest.HandlingException.TodoController;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UnAuthTest extends IntegrationTest{


    @Test
    public void showTodoList() throws Exception{
        this.mockMvc.perform(get("/todo"))
        .andExpect(status().is(FORBIDDEN));
    }

    @Test
    public void insertTodo() throws Exception{
        InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
        insertTodoDTO.setTitle(TestLib.testTodo.title);
        insertTodoDTO.setContext(TestLib.testTodo.context);
        this.mockMvc.perform(post("/todo")
            .content(TestLib.asJsonString(insertTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN));
    }

    @Test
    public void deleteTodo() throws Exception{
        UserEntity testUser = this.makeTestUser();
        Long testTodoIdx = this.makeTodo(testUser).getIdx();
        this.mockMvc.perform(delete("/todo/"+testTodoIdx))
        .andExpect(status().is(FORBIDDEN));
    }

    @Test
    public void updateTodo() throws Exception{
        UserEntity testUser = this.makeTestUser();
        Long testTodoIdx = this.makeTodo(testUser).getIdx();
        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);

        this.mockMvc.perform(delete("/todo/"+testTodoIdx)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN));
    }
}
