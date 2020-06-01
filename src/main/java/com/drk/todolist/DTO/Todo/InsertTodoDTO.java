package com.drk.todolist.DTO.Todo;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InsertTodoDTO {

    @NotEmpty
    private String title;

    @NotEmpty
    private String context;
}