package com.drk.todolist.Entitis;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity(name = "user")
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "user_name", length = 20, nullable = false)
    private String username;

    @Column(length = 128, nullable = false)
    private String password;

    @Column(name = "nick_name", length = 20, nullable = false)
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx")
    private List<TodoEntity> todoEntityList = new ArrayList<>();
}