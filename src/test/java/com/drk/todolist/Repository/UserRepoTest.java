package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Config.RepositoryTest;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Slf4j
public class UserRepoTest extends RepositoryTest {

    @Test
    public void insertTest() throws Exception {
        UserEntity testUserEntity = new UserEntity();
        testUserEntity.setUsername(TestLib.testUser.name);
        testUserEntity.setNickname(TestLib.testUser.nickName);
        testUserEntity.setPassword(passwordEncoder.encode(TestLib.testUser.password));
        UserEntity insertedUserEntity = this.userRepository.save(testUserEntity);
        log.info("inserted UserEntity");
        log.info(insertedUserEntity.toString());
        assertTrue(TestLib.compareUserEntity(insertedUserEntity, testUserEntity));
    }

    @Test
    public void selectTest() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        final UserEntity selectedUserEntity = 
            this.userRepository.findById(testUserEntity.getIdx()).get();
        log.info("selected Entity");
        log.info(selectedUserEntity.toString());
        assertTrue(TestLib.compareUserEntity(selectedUserEntity, testUserEntity));
    }

    @Test
    public void updateTest() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        testUserEntity.setUsername(TestLib.newTestUser.name);
        testUserEntity.setNickname(TestLib.newTestUser.nickName);
        testUserEntity.setPassword(passwordEncoder.encode(TestLib.newTestUser.password));
        final UserEntity updateUserEntity = this.userRepository.save(testUserEntity);
        log.info("updated Entity");
        log.info(updateUserEntity.toString());
        assertTrue(TestLib.compareUserEntity(updateUserEntity, testUserEntity));
    }

    @Test
    public void deleteTest() throws Exception {
        UserEntity testUserEntity = this.makeTestUser();
        log.info(String.format("testUser Idx : %d",testUserEntity.getIdx()));
        this.userRepository.deleteById(testUserEntity.getIdx());
        final boolean isUserExist = this.userRepository.findById(testUserEntity.getIdx()).isPresent();
        assertFalse(isUserExist);
    }
}