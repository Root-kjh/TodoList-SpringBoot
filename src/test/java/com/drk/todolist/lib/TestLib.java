package com.drk.todolist.lib;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class TestLib {

    public final class testUser{
        public final static String name = "test";
        public final static String nickName = "testNickName";
        public final static String password = "testpw";    
    } 

    public final class newTestUser{
        public final static String name = "new user";
        public final static String nickName = "new nickname";
        public final static String password = "newpw";    
    }

    public final class testTodo{
        public final static String title = "test todo";
        public final static String context = "test context";
    }

    public final class newTestTodo{
        public final static String title = "new todo";
        public final static String context = "new context";
    }

    private final static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static boolean compareUserEntity(UserEntity dbUserEntity, UserEntity localUserEntity) throws Exception {
        if (!dbUserEntity.getUsername().equals(localUserEntity.getUsername()))
            throw new Exception("userName is not equals");

        if (!dbUserEntity.getNickname().equals(localUserEntity.getNickname()))
            throw new Exception("userNickName is not equals");
        
        if (!dbUserEntity.getPassword().equals(localUserEntity.getPassword()))
            throw new Exception("userPassword is not equals");
        
        return true;
    }

    public static boolean compareUserEntity(UserEntity userEntity, UserDTO userDTO) throws Exception {
        if (!userEntity.getUsername().equals(userDTO.getUserName()))
            throw new Exception("userName is not equals");

        if (!userEntity.getNickname().equals(userDTO.getNickName()))
            throw new Exception("userNickName is not equals");

        if (!passwordEncoder.matches(userDTO.getPassword(), userEntity.getPassword()))
            throw new Exception("userPassword is not equals");

        return true;
    }

    public static boolean compareUserEntity(UserEntity userEntity, SignupDTO signupDTO) throws Exception {
        if (!userEntity.getUsername().equals(signupDTO.getUserName()))
            throw new Exception("userName is not equals");

        if (!userEntity.getNickname().equals(signupDTO.getNickName()))
            throw new Exception("userNickName is not equals");

        if (!passwordEncoder.matches(signupDTO.getPassword(), userEntity.getPassword()))
            throw new Exception("userPassword is not equals");

        return true;
    }

    public static boolean compareUserEntity(UserEntity userEntity, UpdateUserDTO updateUserDTO) throws Exception {
        if (!userEntity.getUsername().equals(updateUserDTO.getNewUserName()))
            throw new Exception("userName is not equals");

        if (!userEntity.getNickname().equals(updateUserDTO.getNewNickName()))
            throw new Exception("userNickName is not equals");

        return true;
    }

    public static boolean compareTodoEntity(TodoEntity todoEntityA, TodoEntity todoEntityB) throws Exception {
        if (!todoEntityA.getTitle().equals(todoEntityB.getTitle()))
            throw new Exception("todoTitle is not equals");
        
        if (!todoEntityA.getContext().equals(todoEntityB.getContext()))
            throw new Exception("todoContext is not equals");
        
        return true;
    }

    public static boolean compareTodoEntity(TodoEntity todoEntity, TodoDTO todoDTO) throws Exception {
        if (!todoEntity.getTitle().equals(todoDTO.getTitle()))
            throw new Exception("todoTitle is not equals");
        
        if (!todoEntity.getContext().equals(todoDTO.getContext()))
            throw new Exception("todoContext is not equals");
        
        return true;
    }
    
    public static boolean compareTodoEntity(TodoEntity todoEntity, InsertTodoDTO insertTodoDTO) throws Exception {
        if (!todoEntity.getTitle().equals(insertTodoDTO.getTitle()))
            throw new Exception("todoTitle is not equals");
        
        if (!todoEntity.getContext().equals(insertTodoDTO.getContext()))
            throw new Exception("todoContext is not equals");
        
        return true;
    }

    public static boolean compareTodoEntity(TodoEntity todoEntity, UpdateTodoDTO updateTodoDTO) throws Exception {
        if (!todoEntity.getTitle().equals(updateTodoDTO.getNewTitle()))
            throw new Exception("todoTitle is not equals");
        
        if (!todoEntity.getContext().equals(updateTodoDTO.getNewContext()))
            throw new Exception("todoContext is not equals");
        
        return true;
    }
    
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}