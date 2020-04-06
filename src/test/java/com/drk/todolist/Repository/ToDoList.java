package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void insertTest() {
        try {
            this.testUserEntity = makeTestUser();
            makeTodoList(testUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void selectTest() {        
        try {
            this.testUserEntity = makeTestUser();
            this.todoEntity = makeTodoList(testUserEntity);
            final boolean isToDoListSelected = todoRepository.findById(todoEntity.getIdx()).isPresent();
            assertEquals(isToDoListSelected, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void updateTest() {
        final String updatedTodoTitle = "update title";
        try {
            this.testUserEntity = makeTestUser();
            this.todoEntity = makeTodoList(testUserEntity);
            todoEntity.setTitle(updatedTodoTitle);
            final TodoEntity updatedTodo = todoRepository.save(todoEntity);
            assertEquals(updatedTodo.getTitle(), updatedTodoTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void deleteTest() {
        try {
            this.testUserEntity = makeTestUser();
            this.todoEntity = makeTodoList(testUserEntity);
            todoRepository.deleteById(todoEntity.getIdx());
            final boolean isToDoListSelected = todoRepository.findById(todoEntity.getIdx()).isPresent();
            assertEquals(isToDoListSelected, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}