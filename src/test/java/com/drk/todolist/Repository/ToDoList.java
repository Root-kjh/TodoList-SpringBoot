package com.drk.todolist.Repository;

import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.ToDoListRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToDoList extends RepositoryTest{

    @BeforeEach
    @AfterEach
    @Override
    public void clearDB() {
        clearUserDB();
        clearToDoListDB();
    }

    @Test
    @Override
    public void insertTest() {
        try {
            UserEntity testUserEntity = makeTestUser();
            makeTodoList(testUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Override
    public void selectTest() {        try {
        UserEntity testUserEntity = makeTestUser();
        makeTodoList(testUserEntity);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @Test
    @Override
    public void updateTest() {
        try {
            UserEntity testUserEntity = makeTestUser();
            makeTodoList(testUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Override
    public void deleteTest() {
        try {
            UserEntity testUserEntity = makeTestUser();
            makeTodoList(testUserEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}