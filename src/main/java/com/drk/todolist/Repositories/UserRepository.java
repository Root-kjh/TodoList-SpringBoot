package com.drk.todolist.Repositories;


import com.drk.todolist.Entitis.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUser_nameAndPassword(String user_name, String password);
}