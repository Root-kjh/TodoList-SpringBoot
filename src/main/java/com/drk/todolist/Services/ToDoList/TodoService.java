package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Entitis.TodoEntity;


public interface TodoService {
    public List<TodoEntity> selectTodolist(UserJwtDTO userJwtDTO);
    public boolean insertTodo(UserJwtDTO userJwtDTO, String title, String context);
    public boolean deleteTodo(Long todoIdx);
    public boolean updateTodo(Long todoIdx, String newTitle, String newContext);
    public boolean checkTodoOwnership(Long todoIdx, UserJwtDTO userJwtDTO);
}