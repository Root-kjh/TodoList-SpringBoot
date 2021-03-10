package com.drk.todolist.Services.ToDoList;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
        userRepository.save(userEntity);
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
}