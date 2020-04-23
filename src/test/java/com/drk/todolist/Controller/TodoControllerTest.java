package com.drk.todolist.Controller;

import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.lib.TestLib;

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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class TodoControllerTest extends ControllerTestConfigure {

    private String getTodoBasedControllerUrl(String url){
        return UrlMapper.Todo.baseUrl+url;
    }

    @Test
    @Transactional
    public void insertTodo() throws Exception {
        testUserEntity = testLib.makeTestUser();
        TodoDTO newTodoDTO = new TodoDTO();
        newTodoDTO.setTitle(TestLib.testTodo.title);
        newTodoDTO.setContext(TestLib.testTodo.context);
        String jwt = getJwt();

        mockMvc.perform(post(getTodoBasedControllerUrl(UrlMapper.Todo.insertTodo))
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(newTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"))
        .andReturn().getResponse().getContentAsString();

        Long userIdx = userRepository.findByUsername(TestLib.testUser.name).getIdx();
        TodoEntity insertedTodo = todoService.selectTodolist(userIdx).get(0);

        log.info("new TodoDTO : "+newTodoDTO.toString());
        log.info("inserted Todo Entity : "+insertedTodo.toString());

        assertTrue(testLib.compareTodoEntity(insertedTodo, newTodoDTO));
    }

    @Test
    public void showTodo() throws Exception {
        testUserEntity = testLib.makeTestUser();
        String jwt = getJwt();
        testTodoEntity = testLib.makeTodo(testUserEntity);

        String showTodoPageBody = 
            mockMvc.perform(get(getTodoBasedControllerUrl(UrlMapper.Todo.showTodoList))
                .header(TOKEN_HEADER, jwt))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        JSONObject todoListJson = (JSONObject)((JSONArray) new JSONParser().parse(showTodoPageBody)).get(0);
        
        log.info("inserted Todo Entity : "+testTodoEntity.toString());
        log.info("show Todo Entity : "+todoListJson.toString());

        assertEquals(todoListJson.get("title"), testTodoEntity.getTitle());
        assertEquals(todoListJson.get("context"), testTodoEntity.getContext());
    }

    @Test
    @Transactional
    public void updateTodo() throws Exception {
        testUserEntity = testLib.makeTestUser();
        String jwt = getJwt();
        testTodoEntity = testLib.makeTodo(testUserEntity);

        log.info("before Update todo : "+testTodoEntity.toString());

        TodoDTO newTodoDto = new TodoDTO(); 
        newTodoDto.setIdx(testTodoEntity.getIdx());
        newTodoDto.setTitle(TestLib.newTestTodo.title);
        newTodoDto.setContext(TestLib.newTestTodo.context);

        mockMvc.perform(post(getTodoBasedControllerUrl(UrlMapper.Todo.updateTodo))
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(newTodoDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
        
        TodoEntity newTodoEntity = todoService.selectTodolist(testUserEntity.getIdx()).get(0);
        log.info("after Update todo : "+newTodoEntity.toString());
        assertTrue(testLib.compareTodoEntity(newTodoEntity, newTodoDto));
    }

    @Test
    public void deleteTodo() throws Exception {
        final String todoIdxParamName = "todoIdx";
        testUserEntity = testLib.makeTestUser();
        String jwt = getJwt();

        testTodoEntity = testLib.makeTodo(testUserEntity);

        mockMvc.perform(get(getTodoBasedControllerUrl(UrlMapper.Todo.deleteTodo))
            .header(TOKEN_HEADER, jwt)
            .param(todoIdxParamName, testTodoEntity.getIdx().toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        log.info("All todo list : "+todoRepository.findAll().toString());

        assertFalse(todoRepository.findById(testTodoEntity.getIdx()).isPresent());
    }
}