package com.drk.todolist.Repositories;


import com.drk.todolist.Entitis.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsernameAndPassword(String userName, String password);
    void deleteByUsername(String userName);

    @Query("select count(u.username)>0 from user u where u.username = ?1")
    public boolean isExistUser(String userName);
}