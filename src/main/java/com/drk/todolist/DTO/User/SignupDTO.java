package com.drk.todolist.DTO.User;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupDTO {

    @NotEmpty
    private String userName;
    
    @NotEmpty
    private String password;
    
    @NotEmpty
    private String nickName;
}