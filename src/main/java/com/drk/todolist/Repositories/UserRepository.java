package com.drk.todolist.Repositories;


import com.drk.todolist.Entitis.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUser_nameAndPassword(String user_name, String password);
    void deleteByUser_name(String user_name);
    @Query("select count(user_name)>0 from User where user_name=?1")
    public boolean isExistUser(String user_name);
}