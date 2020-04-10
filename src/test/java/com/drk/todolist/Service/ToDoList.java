package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.transaction.Transactional;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Entitis.UserSessionEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;


@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class ToDoList extends ServiceTest{

    private UserSessionEntity testUserSession;

    @BeforeEach
    public void makeTestUser() throws Exception{
        signupTestUser();
        userService.signin(session, testUserName, testUserPassword);
        testUserSession = (UserSessionEntity) session.getAttribute("user");
    }

    
    @Test
    @Transactional
    public void insertTodoTest() throws Exception{
        insertTodo();
        UserEntity userEntity = userRepository.findById(testUserSession.getUserIdx()).get();
        TodoEntity todoEntity = userEntity.getTodoEntityList().get(0);
        assertEquals(todoEntity.getTitle(), testTodoTitle);
    }

    @Test
    @Transactional
    public void selectTodoListTest() throws Exception{
        insertTodo();
        TodoEntity insertedTodoEntity = todoService.selectTodolist(testUserSession.getUserIdx()).get(0);
        assertEquals(insertedTodoEntity.getContext(), testTodoContext);
    }


    @Test
    @Transactional
    public void updateTodoTest() throws Exception{
        final String newTodoTitle = "newTodo";
        final String newTodoContext = "newTodoContext";
        insertTodo();
        Long todoIdx = todoService.selectTodolist(testUserSession.getUserIdx()).get(0).getIdx();
        if (todoService.updateTodo(todoIdx, newTodoTitle, newTodoContext)){
            TodoEntity updatedTodo = todoRepository.findById(todoIdx).get();
            assertEquals(updatedTodo.getTitle(), newTodoTitle);
            assertEquals(updatedTodo.getContext(), newTodoContext);
        }else{
            throw new Exception("update todo fail");
        }
    }

    @Test
    @Transactional
    public void deleteTodoTest() throws Exception{
        insertTodo();
        Long todoIdx = todoService.selectTodolist(testUserSession.getUserIdx()).get(0).getIdx();
        if (todoService.deleteTodo(todoIdx))
            assertFalse(todoRepository.findById(todoIdx).isPresent());
        else
            throw new Exception("delete todo fail");

    }
}