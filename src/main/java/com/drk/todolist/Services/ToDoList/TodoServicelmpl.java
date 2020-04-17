package com.drk.todolist.Services.ToDoList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.drk.todolist.DTO.User.UserJwtDTO;
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

    public boolean isSet(Object object){
        return object!=null;
    }

    @Override
    public boolean checkTodoOwnership(Long todoIdx, UserJwtDTO userJwtDTO){
        try{
            UserEntity userEntity = userRepository.findById(userJwtDTO.getUserIdx()).get();
            for (TodoEntity todoEntity : userEntity.getTodoEntityList()) {
                if (todoEntity.getIdx().equals(todoIdx))
                    return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<TodoEntity> selectTodolist(UserJwtDTO userJwtDTO) {
        List<TodoEntity> todoList = null;
        try{
            todoList = userRepository.findById(userJwtDTO.getUserIdx()).get().getTodoEntityList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return todoList;
    }

    @Override
    public boolean insertTodo(UserJwtDTO userJwtDTO, String title, String context) {
        try{
            UserEntity loginUser = userRepository.findById(userJwtDTO.getUserIdx()).get();
            List<TodoEntity> todoList = loginUser.getTodoEntityList();
            TodoEntity new_todo = new TodoEntity();
            new_todo.setTitle(title);
            new_todo.setContext(context);
            todoList.add(new_todo);
            loginUser.setTodoEntityList(todoList);
            userRepository.save(loginUser);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteTodo(Long todoIdx) {
        try{
            todoRepository.deleteById(todoIdx);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateTodo(Long todoIdx, String newTitle, String newContext) {
        try {
            TodoEntity todo = todoRepository.findById(todoIdx).get();
            if (isSet(newTitle))
                todo.setTitle(newTitle);
            if (isSet(newContext))
                todo.setContext(newContext);
            todoRepository.save(todo);
        return true;    
        }catch(Exception e){
            return false;
        }
    }
}