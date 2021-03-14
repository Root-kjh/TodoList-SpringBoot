package com.drk.todolist.IntegrationTest.HandlingException;

import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import lombok.AllArgsConstructor;

@SpringBootTest
@AutoConfigureMockMvc
@AllArgsConstructor
public class TodoControllerTest{
        
    final static int BAD_REQUEST = 400;
    final static int FORBIDDEN = 403;

    private final IntegrationTest testTool;

    UserEntity testUserEntity;
    String jwt;

    @BeforeEach
    public void setTestUser() throws Exception{
        testUserEntity = testTool.makeTestUser();
        jwt = testTool.getJwt();
    }

    @Nested
    class UnAuthExceptionTest{
        public void insertTodo() throws Exception{}
        public void showTodo() throws Exception{}
        public void updateTodo() throws Exception{}
        public void deleteTodo() throws Exception{}
    }

    @Nested
    class PermissionDeniedExceptionTest extends IntegrationTest{
        public void insertTodo() throws Exception{}
        public void updateTodo() throws Exception{}
        public void deleteTodo() throws Exception{}
    }

    @Nested
    class RequestDataInvalidExceptionTest{
        @Test
        public void insertTodo() throws Exception{
            testTool.makeTestUser();
            String jwt = testTool.getJwt();
            InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
            insertTodoDTO.setTitle(TestLib.testTodo.title);
            String response = testTool.mockMvc.perform(post("/todo")
                .header(testTool.TOKEN_HEADER, jwt)
                .content(TestLib.asJsonString(insertTodoDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(BAD_REQUEST))
            .andReturn().getResponse().getContentAsString();
            String  responseMessage = (String) ((JSONObject) testTool.jsonParser.parse(response)).get("Message");
            
            assertEquals(responseMessage, "Success");
            assertEquals(testTool.todoRepository.count(), 0);
        }
        public void updateTodo() throws Exception{}
        public void deleteTodo() throws Exception{}
    }
}