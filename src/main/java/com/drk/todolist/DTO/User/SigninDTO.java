package com.drk.todolist.DTO.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SigninDTO {
    private String userName;
    private String password;
}