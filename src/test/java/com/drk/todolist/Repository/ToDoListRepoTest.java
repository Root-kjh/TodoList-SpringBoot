package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Slf4j
public class ToDoListRepoTest extends RepositoryTest { 

    @Test
    public void insertTest() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        this.testTodoEntity = new TodoEntity();
        this.testTodoEntity.setTitle(TestLib.testTodo.title);
        this.testTodoEntity.setContext(TestLib.testTodo.context);
        TodoEntity insertedTodoEntity = this.todoRepository.save(this.testTodoEntity);
        log.info("inserted TodoEntity");
        log.info(insertedTodoEntity.toString());
        assertTrue(testLib.compareTodoEntity(this.testTodoEntity, insertedTodoEntity));
    }

    @Test
    public void selectTest() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        this.testTodoEntity = this.testLib.makeTodo(this.testUserEntity);

        final TodoEntity selectedTodoEntity = this.todoRepository.findById(testTodoEntity.getIdx()).get();
        log.info("selected TodoEntity");
        log.info(selectedTodoEntity.toString());
        
        assertTrue(testLib.compareTodoEntity(selectedTodoEntity, this.testTodoEntity));
    }

    @Test
    public void updateTest() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        this.testTodoEntity = this.testLib.makeTodo(testUserEntity);

        this.testTodoEntity.setTitle(TestLib.newTestTodo.title);
        this.testTodoEntity.setContext(TestLib.newTestTodo.context);
        final TodoEntity updatedTodo = this.todoRepository.save(this.testTodoEntity);
        log.info("updated TodoEntity");
        log.info(updatedTodo.toString());
        assertTrue(this.testLib.compareTodoEntity(this.testTodoEntity, updatedTodo));
    }

    @Test
    public void deleteTest() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        this.testTodoEntity = this.testLib.makeTodo(testUserEntity);
        log.info("all todo list : "+this.todoRepository.findAll());
        log.info(String.format("testTodo Idx : %d",this.testTodoEntity.getIdx()));
        this.testUserEntity.getTodoEntityList().remove(this.testTodoEntity);
        this.todoRepository.delete(this.testTodoEntity);
        this.userRepository.save(this.testUserEntity);
        log.info("todo deleted");
        log.info("all todo list : "+this.todoRepository.findAll());
        assertFalse(this.todoRepository.findById(this.testTodoEntity.getIdx()).isPresent());
    }
}