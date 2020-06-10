package com.drk.todolist.Config;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestInit {

    // Repositories
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TodoRepository todoRepository;
    

    // Services

    protected final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // DB Init
    @BeforeEach
    @AfterEach
    public void clearDB(){
        userRepository.deleteAll();
        todoRepository.deleteAll();
    }


    // Make Test user&todo methods
    public UserEntity makeTestUser() throws Exception{
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(TestLib.testUser.name);
        userEntity.setNickname(TestLib.testUser.nickName);
        userEntity.setPassword(passwordEncoder.encode(TestLib.testUser.password));
        UserEntity saveUserEntity = this.userRepository.save(userEntity);
        if (userEntity.getIdx() == saveUserEntity.getIdx())
            return saveUserEntity;
        else
            throw new Exception("userEntity was not save nomarlly");
    }

    public TodoEntity makeTodo(final UserEntity userEntity) throws Exception{
        TodoEntity todo = new TodoEntity();
        todo.setTitle(TestLib.testTodo.title);
        todo.setContext(TestLib.testTodo.context);
        userEntity.addTodo(todo);
        this.userRepository.saveAndFlush(userEntity);
        return userEntity.getTodoEntityList().get(0);
    }

}