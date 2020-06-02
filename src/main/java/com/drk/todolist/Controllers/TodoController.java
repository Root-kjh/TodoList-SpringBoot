package com.drk.todolist.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.ControllerLib;
import com.drk.todolist.Config.Controller.UrlMapper;
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

@RestController
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
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping(UrlMapper.Todo.insertTodo)
    public boolean insertTodo(HttpServletRequest request,Authentication authentication, @RequestBody @Valid InsertTodoDTO insertTodoDTO, Errors errors){
        try{
            if (errors.hasErrors())
                throw new RequestDataInvalidException();
            userEntity = (UserEntity) authentication.getPrincipal();
            return todoService.insertTodo(userEntity.getIdx(), insertTodoDTO);
        } catch (RequestDataInvalidException e){
            throw new RequestDataInvalidException(ControllerLib.getRequestBodyToString(request), UrlMapper.Todo.insertTodo);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping(UrlMapper.Todo.deleteTodo)
    public boolean deleteTodo(HttpServletRequest request, Authentication authentication, @RequestParam Long todoIdx) {
        try{
            userEntity = (UserEntity) authentication.getPrincipal();
            if(todoService.checkTodoOwnership(todoIdx, userEntity.getIdx()))
                return todoService.deleteTodo(todoIdx, userEntity.getIdx());
            else
                throw new UserDataInvalidException();
        } catch (UserDataInvalidException e){
            throw new UserDataInvalidException(ControllerLib.getRequestBodyToString(request), UrlMapper.Todo.deleteTodo);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    @PostMapping(UrlMapper.Todo.updateTodo)
    public boolean updateTodo(HttpServletRequest request, Authentication authentication, @RequestBody @Valid UpdateTodoDTO updateTodoDTO, Errors errors){
        try{
            if (errors.hasErrors())
                throw new RequestDataInvalidException();
            userEntity = (UserEntity) authentication.getPrincipal();
            if (todoService.checkTodoOwnership(updateTodoDTO.getIdx(), userEntity.getIdx()))
                return todoService.updateTodo(updateTodoDTO);
            else
                throw new UserDataInvalidException();

        } catch (RequestDataInvalidException e){
            throw new RequestDataInvalidException(ControllerLib.getRequestBodyToString(request), UrlMapper.Todo.updateTodo);
        } catch (UserDataInvalidException e){
            throw new UserDataInvalidException(ControllerLib.getRequestBodyToString(request), UrlMapper.Todo.updateTodo);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}