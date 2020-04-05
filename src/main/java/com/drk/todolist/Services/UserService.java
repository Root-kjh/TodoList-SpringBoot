package com.drk.todolist.Services;

import com.drk.todolist.Crypto.sha512;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    private sha512 sha512_class=new sha512();

    @Autowired
    UserRepository userRepository;

    public void signin(String user_name, String password){
        password=sha512_class.hash(password);
        UserEntity loginUser = userRepository.findByUser_nameAndPassword(user_name, password);
        if (loginUser.getUser_name() == user_name){
            
        }
    }

    public void signup(String user_name, String password, String nick_name){
        password=sha512_class.hash(password);
        UserEntity userEntity = new UserEntity();
        userEntity.setUser_name(user_name);
        userEntity.setPassword(password);
        userEntity.setNick_name(nick_name);
        userRepository.save(userEntity);
    }
}