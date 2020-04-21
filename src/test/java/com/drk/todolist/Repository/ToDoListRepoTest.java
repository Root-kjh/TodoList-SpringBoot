package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
        this.testUserEntity = TestLib.makeTestUser();
        this.testTodoEntity = new TodoEntity();
        this.testTodoEntity.setTitle(TestLib.testTodo.title);
        this.testTodoEntity.setContext(TestLib.testTodo.context);
        TodoEntity insertedTodoEntity = this.todoRepository.save(this.testTodoEntity);
        log.info("insertedTodoEntity");
        log.info(insertedTodoEntity.toString());
        assertTrue(TestLib.compareTodoEntity(this.testTodoEntity, insertedTodoEntity));
    }

    @Override
    @Test
    public void selectTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = TestLib.makeTestUser();
        this.testTodoEntity = TestLib.makeTodo(testUserEntity);
        log.info("testTodoEntity");
        log.info(this.testTodoEntity.toString());

        final TodoEntity selectedTodoEntity = todoRepository.findById(testTodoEntity.getIdx()).get();
        log.info("selectedTodoEntity");
        log.info(selectedTodoEntity.toString());
        
        assertTrue(TestLib.compareTodoEntity(selectedTodoEntity, this.testTodoEntity));
    }

    @Override
    @Test
    public void updateTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = TestLib.makeTestUser();
        this.testTodoEntity = TestLib.makeTodo(testUserEntity);
        log.info("testTodoEntity");
        log.info(this.testTodoEntity.toString());

        this.testTodoEntity.setTitle(TestLib.newTestTodo.title);
        this.testTodoEntity.setContext(TestLib.newTestTodo.context);
        final TodoEntity updatedTodo = todoRepository.save(this.testTodoEntity);
        
        assertTrue(TestLib.compareTodoEntity(this.testTodoEntity, updatedTodo));
    }

    @Override
    @Test
    public void deleteTest() throws Exception {
        this.testUserEntity = TestLib.makeTestUser();
        this.testTodoEntity = TestLib.makeTodo(testUserEntity);
        todoRepository.deleteById(testTodoEntity.getIdx());
        final boolean isToDoListSelected = todoRepository.findById(testTodoEntity.getIdx()).isPresent();
        assertFalse(isToDoListSelected);
    }
}