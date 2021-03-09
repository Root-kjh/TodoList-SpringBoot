package com.drk.todolist.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserInfoDTO
 {
    private Long userId;
    private String userName;
    private String nickName;
    private String jwt;
}