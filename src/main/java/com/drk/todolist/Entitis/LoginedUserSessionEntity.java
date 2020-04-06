package com.drk.todolist.Entitis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginedUserSessionEntity {
    private Long user_idx;
    private String user_nick_name;
}