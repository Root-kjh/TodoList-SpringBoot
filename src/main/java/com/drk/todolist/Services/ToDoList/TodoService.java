package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;


public interface TodoService {
    public List<TodoEntity> selectTodolist(Long userIdx);
    public boolean insertTodo(Long userIdx, InsertTodoDTO insertTodoDTO);
    public boolean deleteTodo(Long todoIdx, Long userIdx);
    public boolean updateTodo(UpdateTodoDTO updateTodoDTO);
    public boolean checkTodoOwnership(Long todoIdx, Long userIdx);
}