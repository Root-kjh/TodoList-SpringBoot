package com.drk.todolist.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoInfoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.Config.Errors.RequestDataInvalidException;
import com.drk.todolist.Config.Errors.UserDataInvalidException;

import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@CrossOrigin
public class TodoController {

    
    private final TodoService todoService;

    private UserEntity userEntity;

    private final String successMessage = "{\"Message\": \"Success\"}";

    @GetMapping()
    public List<TodoInfoDTO> showTodoList(Authentication authentication) throws Exception{
        userEntity = (UserEntity) authentication.getPrincipal();
        return todoService.showTodoList(userEntity.getIdx());
    }

    @PostMapping()
    public boolean insertTodo(
            HttpServletRequest request,
            Authentication authentication, 
            @RequestBody @Valid InsertTodoDTO insertTodoDTO, 
            Errors errors) throws Exception{
        if (errors.hasErrors())
            throw new RequestDataInvalidException();
        userEntity = (UserEntity) authentication.getPrincipal();
        return todoService.insertTodo(userEntity.getIdx(), insertTodoDTO);
    }

    @DeleteMapping("/{todoId}")
    public String deleteTodo(
            HttpServletRequest request,
            Authentication authentication, 
            @PathVariable Long todoId) throws Exception{
        userEntity = this.todoService.checkOwnerShip(authentication, todoId);
        todoService.deleteTodo(todoId, userEntity.getIdx());
        return this.successMessage;
    }
    
    @PutMapping("/{todoId}")
    public TodoInfoDTO updateTodo(
            HttpServletRequest request, 
            Authentication authentication, 
            @PathVariable Long todoId,
            @RequestBody UpdateTodoDTO updateTodoDTO, 
            Errors errors) throws Exception{
        if (errors.hasErrors())
            throw new RequestDataInvalidException();
        userEntity = this.todoService.checkOwnerShip(authentication, todoId);
        return todoService.updateTodo(updateTodoDTO, todoId);
    }
}