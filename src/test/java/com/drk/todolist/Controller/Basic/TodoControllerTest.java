package com.drk.todolist.Controller.Basic;

import com.drk.todolist.Config.ControllerTest;
import com.drk.todolist.Config.Controller.UrlMapper;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
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
public class TodoControllerTest extends ControllerTest {

    @Test
    @Transactional
    public void insertTodo() throws Exception {
        this.makeTestUser();
        TodoDTO newTodoDTO = new TodoDTO();
        newTodoDTO.setTitle(TestLib.testTodo.title);
        newTodoDTO.setContext(TestLib.testTodo.context);
        String jwt = this.getJwt();

        this.mockMvc.perform(post(UrlMapper.Todo.insertTodo)
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(newTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"))
        .andReturn().getResponse().getContentAsString();

        Long userIdx = this.userRepository.findByUsername(TestLib.testUser.name).getIdx();
        TodoEntity insertedTodo = this.todoService.selectTodolist(userIdx).get(0);

        log.info("new TodoDTO : "+newTodoDTO.toString());
        log.info("inserted Todo Entity : "+insertedTodo.toString());

        assertTrue(TestLib.compareTodoEntity(insertedTodo, newTodoDTO));
    }

    @Test
    public void showTodo() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        String jwt = this.getJwt();
        TodoEntity testTodoEntity = this.makeTodo(testUserEntity);

        String showTodoPageBody = 
        this.mockMvc.perform(get(UrlMapper.Todo.showTodoList)
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
        UserEntity testUserEntity = this.makeTestUser();
        String jwt = this.getJwt();
        TodoEntity testTodoEntity = this.makeTodo(testUserEntity);

        log.info("before Update todo : "+testTodoEntity.toString());

        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setIdx(testTodoEntity.getIdx());
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);

        this.mockMvc.perform(post(UrlMapper.Todo.updateTodo)
            .header(TOKEN_HEADER, jwt)
            .content(TestLib.asJsonString(updateTodoDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
        
        TodoEntity newTodoEntity = this.todoService.selectTodolist(testUserEntity.getIdx()).get(0);
        log.info("after Update todo : "+newTodoEntity.toString());
        assertTrue(TestLib.compareTodoEntity(newTodoEntity, updateTodoDTO));
    }

    @Test
    @Transactional
    public void deleteTodo() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        String jwt = this.getJwt();

        TodoEntity testTodoEntity = this.makeTodo(testUserEntity);
        log.info(" testTodoEntity : "+testTodoEntity.toString());
        this.mockMvc.perform(get(UrlMapper.Todo.deleteTodo)
            .header(TOKEN_HEADER, jwt)
            .param("todoIdx", testTodoEntity.getIdx().toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("true"));

        log.info("All todo list : "+this.todoRepository.findAll().toString());

        assertFalse(this.todoRepository.findById(testTodoEntity.getIdx()).isPresent());
    }
}