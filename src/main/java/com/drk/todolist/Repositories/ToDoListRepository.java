package com.drk.todolist.Repositories;

import com.drk.todolist.Entitis.ToDoListEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoListRepository extends JpaRepository<ToDoListEntity,Long>{

}