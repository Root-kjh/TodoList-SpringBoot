package com.drk.todolist.Services.ToDoList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;

@Service
public class TodoServicelmpl implements TodoService{

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public boolean checkTodoOwnership(Long todoIdx, Long userIdx) throws Exception {
        UserEntity userEntity = userRepository.findById(userIdx).get();
        for (TodoEntity todoEntity : userEntity.getTodoEntityList()) {
            if (todoEntity.getIdx().equals(todoIdx))
                return true;
        }
        return false;
    }

    @Override
    @Transactional
    public List<TodoEntity> selectTodolist(Long userIdx) throws Exception {
        return userRepository.findById(userIdx).get().getTodoEntityList();
    }

    @Override
    @Transactional
    public boolean insertTodo(Long userIdx, InsertTodoDTO insertTodoDTO) throws Exception {
        UserEntity loginUser = userRepository.findById(userIdx).get();
        List<TodoEntity> todoList = loginUser.getTodoEntityList();
        TodoEntity new_todo = new TodoEntity();
        new_todo.setTitle(insertTodoDTO.getTitle());
        new_todo.setContext(insertTodoDTO.getContext());
        todoList.add(new_todo);
        loginUser.setTodoEntityList(todoList);
        userRepository.save(loginUser);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteTodo(Long todoIdx, Long userIdx) throws Exception {
        TodoEntity todoEntity = todoRepository.findById(todoIdx).get();
        UserEntity userEntity = userRepository.findById(userIdx).get();
        userEntity.getTodoEntityList().remove(todoEntity);
        this.todoRepository.delete(todoEntity);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    @Transactional
    public boolean updateTodo(UpdateTodoDTO updateTodoDTO) throws Exception {
        TodoEntity todo = new TodoEntity();
        todo.setIdx(updateTodoDTO.getIdx());
        todo.setTitle(updateTodoDTO.getNewTitle());
        todo.setContext(updateTodoDTO.getNewContext());
        todoRepository.save(todo);
        return true;
    }
}