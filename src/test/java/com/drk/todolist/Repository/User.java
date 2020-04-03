package com.drk.todolist.Repository;

import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class User {

    @Autowired
    UserRepository userRepository;

    @Test
    public void  selectTest(){
        UserEntity userEntity=new UserEntity();
        userEntity.setUserName("test");
        userEntity.setNickName("testNickName");
        userEntity.setPassword("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
        
    }

    @Test 
    public void insertTest(){

    }

    @Test
    public void updateTest(){

    }

    @Test
    public void deleteTest(){

    }

}