package com.drk.todolist.Services.ToDoList;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.drk.todolist.Config.Errors.UserDataInvalidException;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoInfoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class TodoServicelmpl implements TodoService{

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TodoInfoDTO> showTodoList(Long userIdx) {
        List<TodoEntity> todoList = userRepository.findById(userIdx).get().getTodoEntityList();
        List<TodoInfoDTO> todoInfoList = new ArrayList<>();
        for (TodoEntity todoEntity: todoList) {
                todoInfoList.add(new TodoInfoDTO(todoEntity.getIdx(), todoEntity.getTitle(), todoEntity.getContext()));
        }
        return todoInfoList;
    }

    @Override
    @Transactional
    public boolean insertTodo(Long userId, InsertTodoDTO insertTodoDTO) {
        UserEntity userEntity = userRepository.findById(userId).get();
        List<TodoEntity> todoList = userEntity.getTodoEntityList();
        TodoEntity new_todo = new TodoEntity();
        new_todo.setTitle(insertTodoDTO.getTitle());
        new_todo.setContext(insertTodoDTO.getContext());
        todoList.add(new_todo);
        userEntity.setTodoEntityList(todoList);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteTodo(Long todoId, Long userId) {
        TodoEntity todoEntity = todoRepository.findById(todoId).get();
        UserEntity userEntity = userRepository.findById(userId).get();
        userEntity.getTodoEntityList().remove(todoEntity);
        this.todoRepository.delete(todoEntity);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    @Transactional
    public TodoInfoDTO updateTodo(UpdateTodoDTO updateTodoDTO, Long todoId) {
        TodoEntity todoEntity = todoRepository.findById(todoId).get();
        todoEntity.setTitle(updateTodoDTO.getNewTitle());
        todoEntity.setContext(updateTodoDTO.getNewContext());
        todoRepository.save(todoEntity);
        TodoInfoDTO todoInfoDTO = new TodoInfoDTO(todoEntity.getIdx(), todoEntity.getTitle(), todoEntity.getContext());
        return todoInfoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity checkOwnerShip(Authentication authentication, Long todoId) throws UserDataInvalidException{
        Long userId = ((UserEntity) authentication.getPrincipal()).getIdx();
        UserEntity userEntity = this.userRepository.findById(userId).get();
        for (TodoEntity todoEntity : userEntity.getTodoEntityList()) {
            if (todoEntity.getIdx().equals(todoId))
                return userEntity;
        }
        throw new UserDataInvalidException();
    }
}