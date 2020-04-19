package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.Entitis.UserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class todoServiceTest extends ServiceTest{

    @BeforeEach
    public void makeTestUser(){
        UserInfoDTO newUser = new UserInfoDTO();
        newUser.setNickName(testUserNickName);
        newUser.setUserName(testUserName);
        newUser.setPassword(testUserPassword);
        userService.signup(newUser);
    }

    @Test
    public void insertTodoTest(){
        UserEntity testUser = userRepository.findByUsername(testUserName);
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(testTitle);
        todoDTO.setContext(testContext);
        assertTrue(todoService.insertTodo(testUser.getIdx(), todoDTO));
    }

    @Test
    @Transactional
    public void showTodoTest(){
        UserEntity testUser = userRepository.findByUsername(testUserName);
        insertTodoTest();
        assertFalse(todoService.selectTodolist(testUser.getIdx()).isEmpty());
    }

    @Test
    @Transactional
    public void deleteTodoTest(){
        UserEntity testUser = userRepository.findByUsername(testUserName);
        insertTodoTest();
        assertFalse(todoService.selectTodolist(testUser.getIdx()).isEmpty());
        Long todoIdx = todoService.selectTodolist(testUser.getIdx()).get(0).getIdx();
        System.out.println(todoService.selectTodolist(testUser.getIdx()).get(0).getIdx());
        todoService.deleteTodo(todoIdx);
        assertFalse(todoRepository.findById(todoIdx).isPresent());
    }

    @Test
    @Transactional
    public void updateTodoTest(){
        final String newTitle = "updated Title";
        final String newContext = "updated Context";
        UserEntity testUser = userRepository.findByUsername(testUserName);
        insertTodoTest();
        Long todoIdx = todoService.selectTodolist(testUser.getIdx()).get(0).getIdx();
        TodoDTO newTodoDto = new TodoDTO();
        newTodoDto.setTitle(newTitle);
        newTodoDto.setContext(newContext);
        todoService.updateTodo(todoIdx, newTodoDto);
        assertEquals(todoRepository.findById(todoIdx).get().getTitle(), newTitle);
    }
}