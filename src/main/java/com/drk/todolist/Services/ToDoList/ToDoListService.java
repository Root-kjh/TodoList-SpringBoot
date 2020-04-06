package com.drk.todolist.Services.ToDoList;

import java.util.List;

import com.drk.todolist.Entitis.ToDoListEntity;

public interface ToDoListService {
    public List<ToDoListEntity> showUserTodolist(Long user_idx);
    public boolean insertUserTodoList(Long user_idx, String title, String context);
    public boolean deleteUserTodoList(Long todolist_idx);
    public boolean updateUserTodoList(Long todolist_idx, String title, String context);
}