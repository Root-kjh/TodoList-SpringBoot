package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Slf4j
public class ToDoListRepoTest extends RepositoryTest{

    private UserEntity testUserEntity;
    private TodoEntity testTodoEntity;

    @Override
    @Test
    public void insertTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = testLib.makeTestUser();
        this.testTodoEntity = new TodoEntity();
        this.testTodoEntity.setTitle(TestLib.testTodo.title);
        this.testTodoEntity.setContext(TestLib.testTodo.context);
        TodoEntity insertedTodoEntity = this.todoRepository.save(this.testTodoEntity);
        log.info("inserted TodoEntity");
        log.info(insertedTodoEntity.toString());
        assertTrue(TestLib.compareTodoEntity(this.testTodoEntity, insertedTodoEntity));
    }

    @Override
    @Test
    public void selectTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = testLib.makeTestUser();
        this.testTodoEntity = testLib.makeTodo(testUserEntity);

        final TodoEntity selectedTodoEntity = this.todoRepository.findById(testTodoEntity.getIdx()).get();
        log.info("selected TodoEntity");
        log.info(selectedTodoEntity.toString());
        
        assertTrue(TestLib.compareTodoEntity(selectedTodoEntity, this.testTodoEntity));
    }

    @Override
    @Test
    public void updateTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = testLib.makeTestUser();
        this.testTodoEntity = testLib.makeTodo(testUserEntity);

        this.testTodoEntity.setTitle(TestLib.newTestTodo.title);
        this.testTodoEntity.setContext(TestLib.newTestTodo.context);
        final TodoEntity updatedTodo = this.todoRepository.save(this.testTodoEntity);
        log.info("updated TodoEntity");
        log.info(updatedTodo.toString());
        assertTrue(TestLib.compareTodoEntity(this.testTodoEntity, updatedTodo));
    }

    @Override
    @Test
    public void deleteTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = testLib.makeTestUser();
        this.testTodoEntity = testLib.makeTodo(testUserEntity);
        log.info(String.format("testTodo Idx : %d",this.testTodoEntity.getIdx()));
        
        this.todoRepository.deleteById(this.testTodoEntity.getIdx());
        final boolean isToDoListSelected = this.todoRepository.findById(testTodoEntity.getIdx()).isPresent();
        assertFalse(isToDoListSelected);
    }
}