package com.drk.todolist.Repositories;


import com.drk.todolist.Entitis.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserNameAndPassword(String userName, String password);
    void deleteByUserName(String userName);
    @Query("select count(u.userName)>0 from UserEntity u where u.userName = ?1")
    public boolean isExistUser(String userName);
}