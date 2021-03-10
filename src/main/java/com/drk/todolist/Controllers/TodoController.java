package com.drk.todolist.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoInfoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController("/todo")
@RequiredArgsConstructor
@CrossOrigin
public class TodoController {

    
    private final TodoService todoService;

    private final UserService userService;

    private UserEntity userEntity;

    private UserEntity checkOwnerShip(Authentication authentication, Long todoId) throws UserDataInvalidException{
        userEntity = (UserEntity) authentication.getPrincipal();
        for (TodoEntity todoEntity : userEntity.getTodoEntityList()) {
            if (todoEntity.getIdx().equals(todoId))
                return userEntity;
        }
        throw new UserDataInvalidException();
    }

    @GetMapping()
    public List<TodoInfoDTO> showTodoList(Authentication authentication){
        userEntity = (UserEntity) authentication.getPrincipal();
        return todoService.showTodoList(userEntity.getIdx());
    }

    @PostMapping()
    public boolean insertTodo(
            HttpServletRequest request,
            Authentication authentication, 
            @RequestBody @Valid InsertTodoDTO insertTodoDTO, 
            Errors errors) {
        if (errors.hasErrors())
            throw new RequestDataInvalidException();
        userEntity = (UserEntity) authentication.getPrincipal();
        return todoService.insertTodo(userEntity.getIdx(), insertTodoDTO);
    }

    @GetMapping("/{todoId}")
    public boolean deleteTodo(
            HttpServletRequest request,
            Authentication authentication, 
            @RequestParam Long todoId){
        userEntity = checkOwnerShip(authentication, todoId);
        return todoService.deleteTodo(todoId, userEntity.getIdx());
    }
    
    @PostMapping("/{todoId}"))
    public TodoInfoDTO updateTodo(
            HttpServletRequest request, 
            Authentication authentication, 
            @RequestParam Long todoId,
            @RequestBody @Valid UpdateTodoDTO updateTodoDTO, 
            Errors errors){
        if (errors.hasErrors())
            throw new RequestDataInvalidException();
        userEntity = checkOwnerShip(authentication, todoId);
        return todoService.updateTodo(updateTodoDTO, todoId);
    }
}