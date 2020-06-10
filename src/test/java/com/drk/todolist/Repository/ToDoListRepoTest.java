package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Config.RepositoryTest;
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
public class ToDoListRepoTest extends RepositoryTest { 

    @Test
    public void insertTest() throws Exception {
        this.makeTestUser();
        TodoEntity testTodoEntity = new TodoEntity();
        testTodoEntity.setTitle(TestLib.testTodo.title);
        testTodoEntity.setContext(TestLib.testTodo.context);
        TodoEntity insertedTodoEntity = this.todoRepository.save(testTodoEntity);
        log.info("inserted TodoEntity");
        log.info(insertedTodoEntity.toString());
        assertTrue(TestLib.compareTodoEntity(testTodoEntity, insertedTodoEntity));
    }

    @Test
    public void selectTest() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        TodoEntity testTodoEntity = this.makeTodo(testUserEntity);

        final TodoEntity selectedTodoEntity = this.todoRepository.findById(testTodoEntity.getIdx()).get();
        log.info("selected TodoEntity");
        log.info(selectedTodoEntity.toString());
        
        assertTrue(TestLib.compareTodoEntity(selectedTodoEntity, testTodoEntity));
    }

    @Test
    public void updateTest() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        TodoEntity testTodoEntity = this.makeTodo(testUserEntity);

        testTodoEntity.setTitle(TestLib.newTestTodo.title);
        testTodoEntity.setContext(TestLib.newTestTodo.context);
        final TodoEntity updatedTodo = this.todoRepository.save(testTodoEntity);
        log.info("updated TodoEntity");
        log.info(updatedTodo.toString());
        assertTrue(TestLib.compareTodoEntity(testTodoEntity, updatedTodo));
    }

    @Test
    public void deleteTest() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        TodoEntity testTodoEntity = this.makeTodo(testUserEntity);
        log.info("all todo list : "+this.todoRepository.findAll());
        log.info(String.format("testTodo Idx : %d",testTodoEntity.getIdx()));
        testUserEntity.getTodoEntityList().remove(testTodoEntity);
        this.todoRepository.delete(testTodoEntity);
        this.userRepository.save(testUserEntity);
        log.info("todo deleted");
        log.info("all todo list : "+this.todoRepository.findAll());
        assertFalse(this.todoRepository.findById(testTodoEntity.getIdx()).isPresent());
    }
}