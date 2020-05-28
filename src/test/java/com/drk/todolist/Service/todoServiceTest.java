package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TodoServiceTest extends ServiceTest{

    @BeforeEach
    public void makeTestUser() throws Exception{
        this.testUserEntity = this.testLib.makeTestUser();
    }

    @Test
    @Transactional
    public void insertTodoTest() throws Exception {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(TestLib.newTestTodo.title);
        todoDTO.setContext(TestLib.newTestTodo.context);
        assertTrue(this.todoService.insertTodo(this.testUserEntity.getIdx(), todoDTO));

        TodoEntity insertedTodoEntity = this.testUserEntity.getTodoEntityList().get(0);
        assertTrue(this.testLib.compareTodoEntity(insertedTodoEntity, todoDTO));
    }

    @Test
    @Transactional
    public void showTodoTest() throws Exception {
        this.testTodoEntity = this.testLib.makeTodo(this.testUserEntity);
        TodoEntity selectedTodoEntity = this.todoService.selectTodolist(this.testUserEntity.getIdx()).get(0);
        assertTrue(this.testLib.compareTodoEntity(this.testTodoEntity, selectedTodoEntity));
    }

    @Test
    @Transactional
    public void deleteTodoTest() throws Exception {
        this.testTodoEntity = this.testLib.makeTodo(this.testUserEntity);
        this.todoService.deleteTodo(this.testTodoEntity.getIdx(),this.testUserEntity.getIdx());
        assertFalse(this.todoRepository.findById(this.testTodoEntity.getIdx()).isPresent());
    }

    @Test
    @Transactional
    public void updateTodoTest() throws Exception {
        this.testTodoEntity = this.testLib.makeTodo(this.testUserEntity);
        TodoDTO newTodoDto = new TodoDTO();
        newTodoDto.setTitle(TestLib.newTestTodo.title);
        newTodoDto.setContext(TestLib.newTestTodo.context);
        this.todoService.updateTodo(this.testTodoEntity.getIdx(), newTodoDto);
        TodoEntity updatedTodoEntity = this.todoRepository.findById(this.testTodoEntity.getIdx()).get();
        assertTrue(this.testLib.compareTodoEntity(updatedTodoEntity, newTodoDto));
    }
}