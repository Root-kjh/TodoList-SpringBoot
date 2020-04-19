package com.drk.todolist.Controllers;

import java.util.List;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.Services.User.getUserEntityByJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/todo")
@CrossOrigin
public class TodoController {

    @Autowired
    TodoService todoService;

    @Autowired
    UserService userService;

    UserEntity userEntity;

    getUserEntityByJwt getUserEntityClass;

    @GetMapping("/")
    public List<TodoEntity> showTodoList(){
        try{
            userEntity = getUserEntityClass.getUserEntity();
            return todoService.selectTodolist(userEntity.getIdx());
        }
        catch (Exception e){
            return null;
        }
    }

    @PostMapping("/insert")
    public boolean insertTodo(@RequestBody TodoDTO todoDTO){
        try{
            userEntity = getUserEntityClass.getUserEntity();
            return todoService.insertTodo(userEntity.getIdx(), todoDTO);
        }catch (Exception e){
            return false;
        }
    }

    @GetMapping("/delete")
    public boolean deleteTodo(@RequestParam Long todoIdx) {
        try{
            userEntity = getUserEntityClass.getUserEntity();
            return (todoService.checkTodoOwnership(todoIdx, userEntity.getIdx()) && todoService.deleteTodo(todoIdx));
        }catch (Exception e){
            return false;
        }
    }
    
    @PostMapping("/update")
    public boolean updateTodo(@RequestBody TodoDTO newTodo){
        try{
            userEntity = getUserEntityClass.getUserEntity();
            return (todoService.checkTodoOwnership(newTodo.getIdx(), userEntity.getIdx()) && todoService.updateTodo(newTodo.getIdx(), newTodo));
        }catch (Exception e){
            return false;
        }
    }
}