package com.drk.todolist.Service.Basic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import com.drk.todolist.Config.ServiceTest;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TodoServiceTest extends ServiceTest{
    
    UserEntity testUserEntity;

    @BeforeEach
    public void testUserInit() throws Exception{
        this.testUserEntity = this.makeTestUser();
    }

    @Test
    @Transactional
    public void insertTodoTest() throws Exception {
        InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
        insertTodoDTO.setTitle(TestLib.newTestTodo.title);
        insertTodoDTO.setContext(TestLib.newTestTodo.context);
        assertTrue(this.todoService.insertTodo(testUserEntity.getIdx(), insertTodoDTO));

        TodoEntity insertedTodoEntity = this.testUserEntity.getTodoEntityList().get(0);
        assertTrue(TestLib.compareTodoEntity(insertedTodoEntity, insertTodoDTO));
    }

    @Test
    @Transactional
    public void showTodoTest() throws Exception {
        TodoEntity testTodoEntity = this.makeTodo(this.testUserEntity);
        TodoEntity selectedTodoEntity = this.todoService.selectTodolist(this.testUserEntity.getIdx()).get(0);
        assertTrue(TestLib.compareTodoEntity(testTodoEntity, selectedTodoEntity));
    }

    @Test
    @Transactional
    public void deleteTodoTest() throws Exception {
        TodoEntity testTodoEntity = this.makeTodo(this.testUserEntity);
        this.todoService.deleteTodo(testTodoEntity.getIdx(),this.testUserEntity.getIdx());
        assertFalse(this.todoRepository.findById(testTodoEntity.getIdx()).isPresent());
    }

    @Test
    @Transactional
    public void updateTodoTest() throws Exception {
        TodoEntity testTodoEntity = this.makeTodo(this.testUserEntity);
        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setIdx(testTodoEntity.getIdx());
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);
        this.todoService.updateTodo(updateTodoDTO);
        TodoEntity updatedTodoEntity = this.todoRepository.findById(testTodoEntity.getIdx()).get();
        assertTrue(TestLib.compareTodoEntity(updatedTodoEntity, updateTodoDTO));
    }
}