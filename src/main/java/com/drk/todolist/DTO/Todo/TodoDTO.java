package com.drk.todolist.DTO.Todo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoDTO {
    private Long idx;
    private String title;
    private String context;
    private Long userIdx;
}