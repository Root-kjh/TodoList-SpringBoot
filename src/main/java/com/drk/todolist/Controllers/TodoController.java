package com.drk.todolist.Controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.drk.todolist.Entitis.TodoEntity;
import com.drk.todolist.Entitis.UserSessionEntity;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @Autowired
    UserService userService;

    UserSessionEntity userSessionEntity;

    @GetMapping("/")
    public String showTodoList(HttpSession session, Model model){
        try{
            userSessionEntity = userService.getUserSession(session);
        }
        catch (Exception e){
            userService.logout(session);
            return "redirect:/";
        }
            List<TodoEntity> todoList = todoService.selectTodolist(userSessionEntity.getUserIdx());
            model.addAttribute("todoList", todoList);
        return "showTodoList";
    }

    @PostMapping("/insert")
    public String insertTodo(
            HttpSession session,
            @RequestParam String title,
            @RequestParam String context
            ){
        try{
            userSessionEntity = userService.getUserSession(session);
        }catch (Exception e){
            userService.logout(session);
            return "alterMessage/Todo/insertFail";
        }
        todoService.insertTodo(userSessionEntity.getUserIdx(), title, context);
        return "redirect:todo/";
    }

    @GetMapping("/delete")
    public String deleteTodo(HttpSession session, @RequestParam Long todoIdx) {
        try{
            userSessionEntity=userService.getUserSession(session);
            if (todoService.checkTodoOwnership(todoIdx, userSessionEntity.getUserIdx()))
                throw new Exception("User is not todoOwner");
            todoService.deleteTodo(todoIdx);
        }catch (Exception e){
            userService.logout(session);
            return "alterMessage/Todo/deleteFail";
        }
        return "redirect:todo/";
    }
    
    @PostMapping("/update")
    public String updateTodo(HttpSession session, @RequestParam Long todoIdx, @RequestParam String newTitle, @RequestParam String newContext){
        try{
            userSessionEntity=userService.getUserSession(session);
            if (todoService.checkTodoOwnership(todoIdx, userSessionEntity.getUserIdx()))
                throw new Exception("User is not todoOwner");
            todoService.updateTodo(todoIdx, newTitle, newContext);
        }catch (Exception e){
            userService.logout(session);
            return "alterMessage/Todo/updateFail";
        }
        return "redirect:todo/";
    }
}