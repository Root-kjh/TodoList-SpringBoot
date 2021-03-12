package com.drk.todolist.DTO.User;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserDTO {
    @NotNull
    private String nickName;
}