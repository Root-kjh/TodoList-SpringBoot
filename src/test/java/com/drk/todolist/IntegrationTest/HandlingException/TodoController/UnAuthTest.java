package com.drk.todolist.IntegrationTest.HandlingException.TodoController;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UnAuthTest extends IntegrationTest{

    UserEntity testUser;
    Long todoIdx;

    @BeforeEach
    public void setTodo() throws Exception{
        this.testUser = this.makeTestUser();
        this.todoIdx = this.makeTodo(testUser).getIdx();
    }

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

        assertEquals(this.todoRepository.count(), 1);
    }

    @Test
    public void deleteTodo() throws Exception{
        this.mockMvc.perform(delete("/todo/"+this.todoIdx))
        .andExpect(status().is(FORBIDDEN));

        assertEquals(this.todoRepository.count(), 1);
    }

    @Test
    public void updateTodo() throws Exception{
        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);

        this.mockMvc.perform(delete("/todo/"+this.todoIdx)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(FORBIDDEN));

        TodoEntity todo = this.todoRepository.findById(this.todoIdx).get();
        assertEquals(todo.getTitle(), TestLib.testTodo.title);
        assertEquals(todo.getContext(), TestLib.testTodo.context);
    }
}
