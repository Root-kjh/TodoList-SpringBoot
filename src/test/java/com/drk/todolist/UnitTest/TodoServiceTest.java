package com.drk.todolist.UnitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import com.drk.todolist.Config.UnitTest;
import com.drk.todolist.DTO.Todo.InsertTodoDTO;
import com.drk.todolist.DTO.Todo.TodoInfoDTO;
import com.drk.todolist.DTO.Todo.UpdateTodoDTO;
import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TodoServiceTest extends UnitTest{
    
    @Autowired
    public TodoServiceTest(UserRepository userRepository, TodoRepository todoRepository, UserService userService,
            TodoService todoService) {
        super(userRepository, todoRepository, userService, todoService);
    }

    UserEntity testUserEntity;

    @BeforeEach
    public void testUserInit() throws Exception{
        this.testUserEntity = this.makeTestUser();
    }

    @Test
    public void insertTodoTest() throws Exception {
        InsertTodoDTO insertTodoDTO = new InsertTodoDTO();
        insertTodoDTO.setTitle(TestLib.newTestTodo.title);
        insertTodoDTO.setContext(TestLib.newTestTodo.context);
        this.todoService.insertTodo(this.testUserEntity.getIdx(), insertTodoDTO);
        TodoEntity insertedTodoEntity = this.todoRepository.findAll().get(0);
        assertEquals(TestLib.newTestTodo.title, insertedTodoEntity.getTitle());
        assertEquals(TestLib.newTestTodo.context, insertedTodoEntity.getContext());
    }

    @Test
    public void showTodoTest() throws Exception {
        int todoCount = 5;
        for(int i=0; i<todoCount; i++){
            this.makeTodo(this.testUserEntity);
        }
        
        List<TodoInfoDTO> selectedTodoEntity = this.todoService.showTodoList(this.testUserEntity.getIdx());
        for (int i=0; i<todoCount; i++){
            assertEquals(selectedTodoEntity.get(i).getTitle(), TestLib.testTodo.title);
            assertEquals(selectedTodoEntity.get(i).getContext(), TestLib.testTodo.context);
        }
        
    }

    @Test
    public void deleteTodoTest() throws Exception {
        TodoEntity testTodoEntity = this.makeTodo(this.testUserEntity);
        this.todoService.deleteTodo(testTodoEntity.getIdx(),this.testUserEntity.getIdx());
        assertFalse(this.todoRepository.findById(testTodoEntity.getIdx()).isPresent());
    }

    @Test
    public void updateTodoTest() throws Exception {
        Long testTodoIdx = this.makeTodo(this.testUserEntity).getIdx();
        System.out.println(testTodoIdx);
        UpdateTodoDTO updateTodoDTO = new UpdateTodoDTO();
        updateTodoDTO.setNewTitle(TestLib.newTestTodo.title);
        updateTodoDTO.setNewContext(TestLib.newTestTodo.context);
        this.todoService.updateTodo(updateTodoDTO, testTodoIdx);
        TodoEntity updatedTodoEntity = this.todoRepository.findById(testTodoIdx).get();
        assertEquals(updatedTodoEntity.getTitle(), TestLib.newTestTodo.title);
        assertEquals(updatedTodoEntity.getContext(), TestLib.newTestTodo.context);
    }
}