package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;


public interface TodoService {
    public List<TodoEntity> selectTodolist(Long userIdx);
    public boolean insertTodo(Long userIdx, TodoDTO todoDTO);
    public boolean deleteTodo(Long todoIdx, UserEntity userEntity);
    public boolean updateTodo(Long todoIdx, TodoDTO newTodoDTO);
    public boolean checkTodoOwnership(Long todoIdx, Long userIdx);
}