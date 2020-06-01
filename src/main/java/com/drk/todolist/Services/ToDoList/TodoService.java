package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;


public interface TodoService {
    public List<TodoEntity> selectTodolist(Long userIdx) throws Exception;
    public boolean insertTodo(Long userIdx, InsertTodoDTO insertTodoDTO) throws Exception;
    public boolean deleteTodo(Long todoIdx, Long userIdx) throws Exception;
    public boolean updateTodo(UpdateTodoDTO updateTodoDTO) throws Exception;
    public boolean checkTodoOwnership(Long todoIdx, Long userIdx) throws Exception;
}