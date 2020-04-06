package com.drk.todolist.Services.ToDoList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.drk.todolist.Entitis.ToDoListEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.ToDoListRepository;
import com.drk.todolist.Repositories.UserRepository;

@Service
public class ToDoListServicelmpl implements ToDoListService{

    @Autowired
    ToDoListRepository todoListRepository;

    @Autowired
    UserRepository userRepository;

    public boolean isSet(Object object){
        return object!=null;
    }

    @Override
    public List<ToDoListEntity> showUserTodolist(Long user_idx) {
        List<ToDoListEntity> todoList = userRepository.findById(user_idx).get().getToDoListEntities();
        return todoList;
    }

    @Override
    public boolean insertUserTodoList(Long user_idx, String title, String context) {
        try{
            UserEntity loginUser = userRepository.findById(user_idx).get();
            List<ToDoListEntity> todoList = loginUser.getToDoListEntities();
            ToDoListEntity new_todo = new ToDoListEntity();
            new_todo.setTitle(title);
            new_todo.setContext(context);
            todoList.add(new_todo);
            loginUser.setToDoListEntities(todoList);
            userRepository.save(loginUser);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteUserTodoList(Long todolist_idx) {
        try{
            todoListRepository.deleteById(todolist_idx);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    @Override
    public boolean updateUserTodoList(Long todolist_idx, String new_title, String new_context) {
        try{
        ToDoListEntity todo = todoListRepository.findById(todolist_idx).get();
        if (isSet(new_title))
            todo.setTitle(new_title);
        if (isSet(new_context))
            todo.setContext(new_context);
        todoListRepository.save(todo);
        return true;    
        }catch(Exception e){
            return false;
        }
    }
}