package com.drk.todolist.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.drk.todolist.Entitis.TodoEntity;
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
    public void insertTodoTest() throws Exception{
        insertTodo();
        List<TodoEntity> todoList = userRepository.findById(testUserSession.getUserIdx()).get().getTodoEntityList();
        TodoEntity insertedTodo = todoList.get(0);
        assertEquals(insertedTodo.getTitle(), testTodoTitle);
    }

    @Test
    public void selectTodoListTest() throws Exception{
        todoService.selectTodolist(testUserSession.getUserIdx());
    }


    @Test
    public void updateTodoTest() throws Exception{

    }

    @Test
    public void deleteTodoTest() throws Exception{

    }
}