package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.Entitis.TodoEntity;


public interface TodoService {
    public List<TodoEntity> selectTodolist(Long userIdx);
    public boolean insertTodo(Long userIdx, String title, String context);
    public boolean deleteTodo(Long todoIdx);
    public boolean updateTodo(Long todoIdx, String newTitle, String newContext);
}