package com.drk.todolist.Config;

import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;

import lombok.Builder;

public class UnitTest extends TestInit {

    protected final UserService userService;

    protected final TodoService todoService;

    @Builder
    public UnitTest(UserRepository userRepository, TodoRepository todoRepository, UserService userService, TodoService todoService) {
        super(userRepository, todoRepository);
        this.userService = userService;
        this.todoService = todoService;
    }

}