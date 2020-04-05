package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.drk.todolist.Entitis.ToDoListEntity;
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
    private ToDoListEntity todoListEntity;

    @Override
    @BeforeEach
    @AfterEach
    public void clearDB() {
        clearUserDB();
        clearToDoListDB();
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
            this.todoListEntity = makeTodoList(testUserEntity);
            final boolean isToDoListSelected = toDoListRepository.findById(todoListEntity.getIdx()).isPresent();
            assertEquals(isToDoListSelected, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void updateTest() {
        final String updateToDoListTitle = "update title";
        try {
            this.testUserEntity = makeTestUser();
            this.todoListEntity = makeTodoList(testUserEntity);
            todoListEntity.setTitle(updateToDoListTitle);
            final ToDoListEntity updatedToDoList = toDoListRepository.save(todoListEntity);
            assertEquals(updatedToDoList.getTitle(), updateToDoListTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void deleteTest() {
        try {
            this.testUserEntity = makeTestUser();
            this.todoListEntity = makeTodoList(testUserEntity);
            toDoListRepository.deleteById(todoListEntity.getIdx());
            final boolean isToDoListSelected = toDoListRepository.findById(todoListEntity.getIdx()).isPresent();
            assertEquals(isToDoListSelected, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}