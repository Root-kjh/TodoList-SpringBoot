package com.drk.todolist.DTO.User;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyPasswordDTO {
    
    @NotNull
    private String password;
}
