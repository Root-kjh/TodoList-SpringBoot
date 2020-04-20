package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private UserEntity testUserEntity;

    @Override
    @BeforeEach
    @AfterEach
    public void clearDB(){
        userRepository.deleteAll();
    }

    @Override
    @Test 
    public void insertTest() throws Exception {
        makeTestUser();
    }

    @Override
    @Test
    public void selectTest() throws Exception {
        this.testUserEntity = makeTestUser();
        final boolean isUserSelected = userRepository.findById(testUserEntity.getIdx()).isPresent();
        assertTrue(isUserSelected);
    }

    @Override
    @Test
    public void updateTest() throws Exception {
        final String updateUserName = "updateTest";
        this.testUserEntity = makeTestUser();
        testUserEntity.setUsername(updateUserName);
        final UserEntity updateUserEntity = userRepository.save(testUserEntity);
        assertEquals(updateUserEntity.getUsername(), updateUserName);
    }

    @Override
    @Test
    public void deleteTest() throws Exception {
        this.testUserEntity = makeTestUser();
        userRepository.deleteById(testUserEntity.getIdx());
        final boolean isUserSelected = userRepository.findById(testUserEntity.getIdx()).isPresent();
        assertFalse(isUserSelected);
    }
}