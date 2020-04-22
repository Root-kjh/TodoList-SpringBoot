package com.drk.todolist.DTO.Todo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodoDTO {
    private Long idx;
    private String title;
    private String context;
    private Long userIdx;
}