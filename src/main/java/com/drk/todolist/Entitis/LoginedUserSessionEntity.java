package com.drk.todolist.Entitis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginedUserSessionEntity {
    private Long userIdx;
    private String userNickName;
}