package com.drk.todolist.Repository;

import java.util.List;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class RepositoryTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TodoRepository todoRepository;

    public void clearTodoDB(){
        todoRepository.deleteAll();
    }

    public void clearUserDB(){
        userRepository.deleteAll();
    }

    public UserEntity makeTestUser() throws Exception{
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("test");
        userEntity.setNickName("testNickName");
        userEntity.setPassword("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
        UserEntity saveUserEntity = userRepository.save(userEntity);
        if (userEntity.getIdx() == saveUserEntity.getIdx())
            return saveUserEntity;
        else
            throw new Exception("userEntity was not save nomarlly");
    }

    public TodoEntity makeTodoList(UserEntity userEntity) throws Exception{
        TodoEntity toDoList = new TodoEntity();
        toDoList.setTitle("test todolist");
        toDoList.setContext("test context");
        
        List<TodoEntity> toDoLists = userEntity.getTodoEntityList();
        toDoLists.add(toDoList);
        userEntity.setTodoEntityList(toDoLists);
        userRepository.save(userEntity);
        return toDoList;
    }

    public void clearDB(){}
    public void insertTest(){}
    public void  selectTest(){}
    public void updateTest(){}
    public void deleteTest(){}
}