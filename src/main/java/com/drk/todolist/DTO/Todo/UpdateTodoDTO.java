package com.drk.todolist.DTO.Todo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateTodoDTO {

    @NotNull
    private Long idx;

    @NotEmpty
    private String newTitle;

    @NotEmpty
    private String newContext;
}