package com.drk.todolist.Repositories;


import com.drk.todolist.Entitis.TodoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity,Long>{

}