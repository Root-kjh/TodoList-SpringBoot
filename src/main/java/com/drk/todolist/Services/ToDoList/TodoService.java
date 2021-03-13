package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoInfoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.UserEntity;

import org.springframework.security.core.Authentication;


public interface TodoService {
    public List<TodoInfoDTO> showTodoList(Long userId);
    public UserEntity checkOwnerShip(Authentication authentication, Long todoId) throws UserDataInvalidException;
    public boolean insertTodo(Long userId, InsertTodoDTO insertTodoDTO);
    public boolean deleteTodo(Long todoId, Long userId);
    public TodoInfoDTO updateTodo(UpdateTodoDTO updateTodoDTO, Long todoId);
}