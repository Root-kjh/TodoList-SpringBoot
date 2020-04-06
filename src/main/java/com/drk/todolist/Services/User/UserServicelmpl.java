package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

import com.drk.todolist.Crypto.sha512;
import com.drk.todolist.Entitis.LoginedUserSessionEntity;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServicelmpl implements UserService {

    private sha512 sha512_class=new sha512();

    @Autowired
    UserRepository userRepository;

    public boolean isSet(Object object){
        return object!=null;
    }

    public boolean userDuplicateCheck(String user_name){
        return userRepository.isExistUser(user_name);
    }
    

    @Override
    public boolean signin(HttpSession session, String user_name, String password){
        password=sha512_class.hash(password);
        UserEntity loginUser = userRepository.findByUser_nameAndPassword(user_name, password);
        if (loginUser.getUser_name() == user_name){
            LoginedUserSessionEntity loginedUserSessionEntity = new LoginedUserSessionEntity();
            loginedUserSessionEntity.setUser_idx(loginUser.getIdx());
            loginedUserSessionEntity.setUser_nick_name(loginUser.getNick_name());
            session.setAttribute("user", loginedUserSessionEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean signup(String user_name, String password, String nick_name){
        try{
            password=sha512_class.hash(password);
            UserEntity userEntity = new UserEntity();
            userEntity.setUser_name(user_name);
            userEntity.setPassword(password);
            userEntity.setNick_name(nick_name);
            userRepository.save(userEntity);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean logout(HttpSession session) {
        try{
            session.removeAttribute("user");
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean userinfoUpdate(HttpSession session,String new_user_name, String new_nick_name, String new_password, String password) {
        try {

            LoginedUserSessionEntity loginedUserSessionEntity = (LoginedUserSessionEntity) session.getAttribute("user");
            UserEntity loginedUser = userRepository.findById(loginedUserSessionEntity.getUser_idx()).get();
            String loginedUserPassword = loginedUser.getPassword();
            if (loginedUserPassword!=password)
                return false;
            else{
                if (isSet(new_nick_name))
                    loginedUser.setNick_name(new_nick_name);
                if (isSet(new_user_name) && !userRepository.isExistUser(new_user_name)){
                    loginedUser.setUser_name(new_user_name);
                }
                if (isSet(new_password))
                    loginedUser.setPassword(new_password);

                userRepository.save(loginedUser);
                if (isSet(new_nick_name))
                    loginedUserSessionEntity.setUser_nick_name(new_nick_name);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}