package com.drk.todolist.lib;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.DTO.User.UserDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestLib {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public final static class testUser{
        public final static String name = "test";
        public final static String nickName = "testNickName";
        public final static String password = "testpw";    
    } 

    public final static class newTestUser{
        public final static String name = "new user";
        public final static String nickName = "new nickname";
        public final static String password = "newpw";    
    }

    public final static class testTodo{
        public final static String title = "test todo";
        public final static String context = "test context";
    }

    public final static class newTestTodo{
        public final static String title = "new todo";
        public final static String context = "new context";
    }

    public UserEntity makeTestUser() throws Exception{
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(testUser.name);
        userEntity.setNickname(testUser.nickName);
        userEntity.setPassword(passwordEncoder.encode(testUser.password));
        UserEntity saveUserEntity = this.userRepository.save(userEntity);
        if (userEntity.getIdx() == saveUserEntity.getIdx())
            return saveUserEntity;
        else
            throw new Exception("userEntity was not save nomarlly");
    }

    public TodoEntity makeTodo(final UserEntity userEntity) throws Exception{
        TodoEntity todo = new TodoEntity();
        todo.setTitle(testTodo.title);
        todo.setContext(testTodo.context);
        userEntity.getTodoEntityList().add(todo);
        this.userRepository.save(userEntity);
        return todo;
    }

    public boolean compareUserEntity(UserEntity dbUserEntity, UserEntity localUserEntity) throws Exception {
        if (!dbUserEntity.getUsername().equals(localUserEntity.getUsername()))
            throw new Exception("userName is not equals");

        if (!dbUserEntity.getNickname().equals(localUserEntity.getNickname()))
            throw new Exception("userNickName is not equals");
        
        if (!dbUserEntity.getPassword().equals(localUserEntity.getPassword()))
            throw new Exception("userPassword is not equals");
        
        return true;
    }

    public boolean compareUserEntity(UserEntity userEntity, UserDTO userDTO) throws Exception {
        if (!userEntity.getUsername().equals(userDTO.getUserName()))
            throw new Exception("userName is not equals");

        if (!userEntity.getNickname().equals(userDTO.getNickName()))
            throw new Exception("userNickName is not equals");

        if (!passwordEncoder.matches(userDTO.getPassword(), userEntity.getPassword()))
            throw new Exception("userPassword is not equals");

        return true;
    }

    public boolean compareTodoEntity(TodoEntity todoEntityA, TodoEntity todoEntityB) throws Exception {
        if (!todoEntityA.getTitle().equals(todoEntityB.getTitle()))
            throw new Exception("todoTitle is not equals");
        
        if (!todoEntityA.getContext().equals(todoEntityB.getContext()))
            throw new Exception("todoContext is not equals");
        
        return true;
    }

    public boolean compareTodoEntity(TodoEntity todoEntity, TodoDTO todoDTO) throws Exception {
        if (!todoEntity.getTitle().equals(todoDTO.getTitle()))
            throw new Exception("todoTitle is not equals");
        
        if (!todoEntity.getContext().equals(todoDTO.getContext()))
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