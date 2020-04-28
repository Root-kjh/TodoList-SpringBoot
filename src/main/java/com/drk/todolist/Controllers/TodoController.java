package com.drk.todolist.Controllers;

import java.util.List;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.Config.Controller.UrlMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(UrlMapper.Todo.baseUrl)
@CrossOrigin
public class TodoController {

    @Autowired
    TodoService todoService;

    @Autowired
    UserService userService;

    UserEntity userEntity;

    @GetMapping(UrlMapper.Todo.showTodoList)
    public List<TodoEntity> showTodoList(Authentication authentication){
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            return todoService.selectTodolist(userEntity.getIdx());
        }
        catch (Exception e){
            return null;
        }
    }

    @PostMapping(UrlMapper.Todo.insertTodo)
    public boolean insertTodo(Authentication authentication, @RequestBody TodoDTO todoDTO){
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            return todoService.insertTodo(userEntity.getIdx(), todoDTO);
        }catch (Exception e){
            return false;
        }
    }

    @GetMapping(UrlMapper.Todo.deleteTodo)
    public boolean deleteTodo(Authentication authentication, @RequestParam Long todoIdx) {
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            if(todoService.checkTodoOwnership(todoIdx, userEntity.getIdx()))
                return todoService.deleteTodo(todoIdx, userEntity);
            return false;
        }catch (Exception e){
            return false;
        }
    }
    
    @PostMapping(UrlMapper.Todo.updateTodo)
    public boolean updateTodo(Authentication authentication, @RequestBody TodoDTO newTodo){
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            return (todoService.checkTodoOwnership(newTodo.getIdx(), userEntity.getIdx()) && todoService.updateTodo(newTodo.getIdx(), newTodo));
        }catch (Exception e){
            return false;
        }
    }
}