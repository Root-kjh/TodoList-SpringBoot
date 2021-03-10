package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoInfoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;


public interface TodoService {
    public List<TodoInfoDTO> showTodoList(Long userId);
    public boolean insertTodo(Long userId, InsertTodoDTO insertTodoDTO);
    public boolean deleteTodo(Long todoId, Long userId);
    public TodoInfoDTO updateTodo(UpdateTodoDTO updateTodoDTO, Long todoId);
}