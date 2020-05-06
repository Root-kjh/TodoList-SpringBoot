package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Slf4j
public class UserRepoTest extends RepositoryTest{

    final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test 
    public void insertTest() throws Exception {
        this.testUserEntity = new UserEntity();
        this.testUserEntity.setUsername(TestLib.testUser.name);
        this.testUserEntity.setNickname(TestLib.testUser.nickName);
        this.testUserEntity.setPassword(passwordEncoder.encode(TestLib.testUser.password));
        UserEntity insertedUserEntity = this.userRepository.save(this.testUserEntity);
        log.info("inserted UserEntity");
        log.info(insertedUserEntity.toString());
        assertTrue(this.testLib.compareUserEntity(insertedUserEntity, this.testUserEntity));
    }

    @Test
    public void selectTest() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        final UserEntity selectedUserEntity = 
            this.userRepository.findById(this.testUserEntity.getIdx()).get();
        log.info("selected Entity");
        log.info(selectedUserEntity.toString());
        assertTrue(testLib.compareUserEntity(selectedUserEntity, this.testUserEntity));
    }

    @Test
    public void updateTest() throws Exception {
        this.testUserEntity = this.testLib.makeTestUser();
        this.testUserEntity.setUsername(TestLib.newTestUser.name);
        this.testUserEntity.setNickname(TestLib.newTestUser.nickName);
        this.testUserEntity.setPassword(passwordEncoder.encode(TestLib.newTestUser.password));
        final UserEntity updateUserEntity = this.userRepository.save(this.testUserEntity);
        log.info("updated Entity");
        log.info(updateUserEntity.toString());
        assertTrue(this.testLib.compareUserEntity(updateUserEntity, this.testUserEntity));
    }

    @Test
    public void deleteTest() throws Exception {
        this.testUserEntity = testLib.makeTestUser();
        log.info(String.format("testUser Idx : %d",this.testUserEntity.getIdx()));
        this.userRepository.deleteById(this.testUserEntity.getIdx());
        final boolean isUserSelected = this.userRepository.findById(this.testUserEntity.getIdx()).isPresent();
        assertFalse(isUserSelected);
    }
}