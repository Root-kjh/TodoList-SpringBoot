package com.drk.todolist.Controller;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TodoControllerTest extends ControllerTestConfigure {

    @Test
    @Transactional
    public void insertTodo() throws Exception{
        makeTestUser();
        TodoDTO newTodoDto = new TodoDTO();
        newTodoDto.setTitle(testTodoTitle);
        newTodoDto.setContext(testTodoContext);
        String jwt = getJwt();

        String insertTodoPageBody = mockMvc.perform(post("/todo/insert")
            .header("X-AUTH-TOKEN", jwt)
            .content(asJsonString(newTodoDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        assertEquals(insertTodoPageBody, "true");

        Long userIdx = userRepository.findByUsername(testUserName).getIdx();
        TodoEntity insertedTodo = todoService.selectTodolist(userIdx).get(0);
        assertEquals(insertedTodo.getTitle(), testTodoTitle);
        assertEquals(insertedTodo.getContext(), testTodoContext);
    }

    @Test
    public void showTodo() throws Exception{
        makeTestUser();
        String jwt = getJwt();
        Long userIdx = userRepository.findByUsername(testUserName).getIdx();
        makeTestTodo(userIdx);

        String showTodoPageBody = mockMvc.perform(get("/todo/show")
            .header("X-AUTH-TOKEN", jwt))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

        JSONObject todoListJson = (JSONObject)((JSONArray) new JSONParser().parse(showTodoPageBody)).get(0);
        assertEquals(todoListJson.get("title"), testTodoTitle);
        assertEquals(todoListJson.get("context"), testTodoContext);
    }

    @Test
    public void updateTodo() throws Exception{
        final String newTodoTitle = "new Todo";
        final String newTodoContext = "new Context";
        
        makeTestUser();
        String jwt = getJwt();
        Long userIdx = userRepository.findByUsername(testUserName).getIdx();
        makeTestTodo(userIdx);
        TodoEntity todoEntity = todoService.selectTodolist(userIdx).get(0);

        TodoDTO newTodoDto = new TodoDTO(); 
        newTodoDto.setIdx(todoEntity.getIdx());
        newTodoDto.setTitle(newTodoTitle);
        newTodoDto.setContext(newTodoContext);

        mockMvc.perform(post("/todo/update")
            .header("X-AUTH-TOKEN", jwt)
            .content(asJsonString(newTodoDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        TodoEntity newTodoEntity = todoService.selectTodolist(userIdx).get(0);
        assertEquals(newTodoEntity.getTitle(), newTodoTitle);
        assertEquals(newTodoEntity.getContext(), newTodoContext);
    }

    @Test
    @Transactional
    public void deleteTodo() throws Exception{
        makeTestUser();
        String jwt = getJwt();
        UserEntity userEntity = userRepository.findByUsername(testUserName);

        makeTestTodo(userEntity.getIdx());
        Long todoIdx = todoService.selectTodolist(userEntity.getIdx()).get(0).getIdx();

        mockMvc.perform(get("/todo/delete")
            .header("X-AUTH-TOKEN", jwt)
            .param("todoIdx", todoIdx.toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        assertFalse(todoRepository.findById(todoIdx).isPresent());
    }
}