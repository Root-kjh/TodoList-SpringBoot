package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.drk.todolist.Entitis.UserEntity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class User extends RepositoryTest{
    @Override
    @BeforeEach
    @AfterEach
    public void clearDB(){
        clearUserDB();
    }

    @Override
    @Test 
    public void insertTest(){
        try {
            makeTestUser();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Test
    public void selectTest() {
        try {
            final UserEntity testUserEntity = makeTestUser();
            final boolean isUserSelected = userRepository.findById(testUserEntity.getIdx()).isPresent();
            assertEquals(isUserSelected, true);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void updateTest() {
        final String updateUserName = "updateTest";

        try {
            final UserEntity testUserEntity = makeTestUser();
            testUserEntity.setUser_name(updateUserName);
            final UserEntity updateUserEntity = userRepository.save(testUserEntity);
            assertEquals(updateUserEntity.getUser_name(), updateUserName);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void deleteTest() {
        try {
            final UserEntity testUserEntity = makeTestUser();
            userRepository.deleteById(testUserEntity.getIdx());
            final boolean isUserSelected = userRepository.findById(testUserEntity.getIdx()).isPresent();
            assertEquals(isUserSelected, false);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}