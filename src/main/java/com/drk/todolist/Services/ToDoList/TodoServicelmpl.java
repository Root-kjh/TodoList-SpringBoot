package com.drk.todolist.Services.ToDoList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

import com.drk.todolist.DTO.Todo.TodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.lib.VariablesLib;

@Service
public class TodoServicelmpl implements TodoService{

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public boolean checkTodoOwnership(Long todoIdx, Long userIdx){
        try{
            UserEntity userEntity = userRepository.findById(userIdx).get();
            for (TodoEntity todoEntity : userEntity.getTodoEntityList()) {
                if (todoEntity.getIdx()==todoIdx)
                    return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    @Transactional
    public List<TodoEntity> selectTodolist(Long userIdx) {
        return userRepository.findById(userIdx).get().getTodoEntityList();
    }

    @Override
    @Transactional
    public boolean insertTodo(Long userIdx, TodoDTO todo) {
        try{
            UserEntity loginUser = userRepository.findById(userIdx).get();
            List<TodoEntity> todoList = loginUser.getTodoEntityList();
            TodoEntity new_todo = new TodoEntity();
            new_todo.setTitle(todo.getTitle());
            new_todo.setContext(todo.getContext());
            todoList.add(new_todo);
            loginUser.setTodoEntityList(todoList);
            userRepository.save(loginUser);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteTodo(Long todoIdx) {
        try{
            todoRepository.deleteByIdx(todoIdx);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateTodo(Long todoIdx, TodoDTO newTodoDto) {
        try {
            TodoEntity todo = todoRepository.findById(todoIdx).get();
            if (VariablesLib.isSet(newTodoDto.getTitle()))
                todo.setTitle(newTodoDto.getTitle());
            if (VariablesLib.isSet(newTodoDto.getContext()))
                todo.setContext(newTodoDto.getContext());
            todoRepository.save(todo);
        return true;    
        }catch(Exception e){
            return false;
        }
    }
}