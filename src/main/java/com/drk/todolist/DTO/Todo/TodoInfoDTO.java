package com.drk.todolist.DTO.Todo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TodoInfoDTO {
    private Long idx;
    private String title;
    private String context;
}