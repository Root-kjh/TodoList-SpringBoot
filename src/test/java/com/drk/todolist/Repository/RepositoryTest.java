package com.drk.todolist.Repository;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TodoRepository todoRepository;

    UserEntity testUserEntity;
    TodoEntity testTodoEntity;

    TestLib testLib;

    @BeforeEach
    public void settingLibs(){
        testLib = new TestLib(userRepository, todoRepository);
    }

    @BeforeEach
    @AfterEach
    public void clearDB(){
        userRepository.deleteAll();
        todoRepository.deleteAll();
    }

    public void insertTest() throws Exception {}
    public void  selectTest() throws Exception {}
    public void updateTest() throws Exception{}
    public void deleteTest() throws Exception {}
}