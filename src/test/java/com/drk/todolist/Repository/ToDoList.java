package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ToDoList extends RepositoryTest{

    private UserEntity testUserEntity;
    private TodoEntity todoEntity;

    @Override
    @BeforeEach
    @AfterEach
    public void clearDB() {
        clearUserDB();
        clearTodoDB();
    }

    @Override
    @Test
    public void insertTest() throws Exception {
        this.testUserEntity = makeTestUser();
        makeTodo(testUserEntity);
    }

    @Override
    @Test
    public void selectTest() throws Exception {
        this.testUserEntity = makeTestUser();
        this.todoEntity = makeTodo(testUserEntity);
        final boolean isToDoListSelected = todoRepository.findById(todoEntity.getIdx()).isPresent();
        assertTrue(isToDoListSelected);
    }

    @Override
    @Test
    public void updateTest() throws Exception {
        final String updatedTodoTitle = "update title";
        this.testUserEntity = makeTestUser();
        this.todoEntity = makeTodo(testUserEntity);
        todoEntity.setTitle(updatedTodoTitle);
        final TodoEntity updatedTodo = todoRepository.save(todoEntity);
        assertEquals(updatedTodo.getTitle(), updatedTodoTitle);
    }

    @Override
    @Test
    public void deleteTest() throws Exception {
        this.testUserEntity = makeTestUser();
        this.todoEntity = makeTodo(testUserEntity);
        todoRepository.deleteById(todoEntity.getIdx());
        final boolean isToDoListSelected = todoRepository.findById(todoEntity.getIdx()).isPresent();
        assertFalse(isToDoListSelected);
    }
}