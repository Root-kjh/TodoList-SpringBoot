package com.drk.todolist.DTO.Todo;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InsertTodoDTO {

    @NotNull
    private String title;

    @NotNull
    private String context;
}