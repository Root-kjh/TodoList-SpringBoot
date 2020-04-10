package com.drk.todolist.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSessionDTO {
    private Long userIdx;
    private String userNickName;
    private String userName;
}