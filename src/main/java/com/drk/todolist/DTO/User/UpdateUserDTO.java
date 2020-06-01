package com.drk.todolist.DTO.User;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateUserDTO {

    @NotEmpty
    private String newUserName;
    
    @NotEmpty
    private String newPassword;
    
    @NotEmpty
    private String newNickName;
}