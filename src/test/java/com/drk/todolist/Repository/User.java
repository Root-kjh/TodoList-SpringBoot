package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;

import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class User {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DataSource datasource;

    @BeforeEach
    @AfterEach
    public void clearDB(){
        userRepository.deleteAll();
    }

    private UserEntity insertTestUser() throws Exception{
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_name("test");
        userEntity.setNick_name("testNickName");
        userEntity.setPassword("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
        UserEntity saveUserEntity = userRepository.save(userEntity);
        if (userEntity.getIdx()==saveUserEntity.getIdx())
            return saveUserEntity;
        else
            throw new Exception("userEntity was not save nomarlly");
    }

    @Test 
    public void insertTest(){
        try {
            insertTestUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
            
    }

    @Test
    public void  selectTest(){
        try{
            final UserEntity testUserEntity = insertTestUser();
            final boolean isUserSelected = userRepository.findById(testUserEntity.getIdx()).isPresent();
            assertEquals(isUserSelected, true);                
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest(){
        final String updateUserName = "updateTest";

        try {
            UserEntity testUserEntity = insertTestUser();
            testUserEntity.setUser_name(updateUserName);
            final UserEntity updateUserEntity = userRepository.save(testUserEntity);
            assertEquals(updateUserEntity.getUser_name(), updateUserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTest(){
        try {
            UserEntity testUserEntity = insertTestUser();
            userRepository.deleteById(testUserEntity.getIdx());
            final boolean isUserSelected = userRepository.findById(testUserEntity.getIdx()).isPresent();
            assertEquals(isUserSelected, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}