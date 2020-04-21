package com.drk.todolist.Repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Slf4j
public class UserRepoTest extends RepositoryTest{

    private UserEntity testUserEntity;

    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Test 
    public void insertTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = new UserEntity();
        this.testUserEntity.setUsername(TestLib.testUser.name);
        this.testUserEntity.setNickname(TestLib.testUser.nickName);
        this.testUserEntity.setPassword(passwordEncoder.encode(TestLib.testUser.password));
        UserEntity insertedUserEntity = userRepository.save(this.testUserEntity);
        log.info("inserted UserEntity");
        log.info(insertedUserEntity.toString());
        assertTrue(TestLib.compareUserEntity(insertedUserEntity, this.testUserEntity));
    }

    @Override
    @Test
    public void selectTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = TestLib.makeTestUser();
        final UserEntity selectedUserEntity = userRepository.findById(this.testUserEntity.getIdx()).get();
        assertTrue(TestLib.compareUserEntity(selectedUserEntity, this.testUserEntity));
    }

    @Override
    @Test
    public void updateTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = TestLib.makeTestUser();
        this.testUserEntity.setUsername(TestLib.newTestUser.name);
        this.testUserEntity.setNickname(TestLib.newTestUser.nickName);
        this.testUserEntity.setPassword(passwordEncoder.encode(TestLib.newTestUser.password));
        final UserEntity updateUserEntity = userRepository.save(this.testUserEntity);
        assertTrue(TestLib.compareUserEntity(updateUserEntity, this.testUserEntity));
    }

    @Override
    @Test
    public void deleteTest() throws Exception {
        TestLib.drawLogGuideLine();
        this.testUserEntity = TestLib.makeTestUser();
        userRepository.deleteById(this.testUserEntity.getIdx());
        final boolean isUserSelected = userRepository.findById(this.testUserEntity.getIdx()).isPresent();
        assertFalse(isUserSelected);
    }
}