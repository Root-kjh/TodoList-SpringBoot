package com.drk.todolist.Repository;

import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TodoRepository todoRepository;

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