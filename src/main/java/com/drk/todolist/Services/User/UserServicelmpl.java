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
    public boolean signin(HttpSession session, String userName, String password) {
        password=sha512_class.hash(password);
        UserEntity loginUser = userRepository.findByUserNameAndPassword(userName, password);
        if (loginUser.getUserName() == userName) {
            LoginedUserSessionEntity loginedUserSessionEntity = new LoginedUserSessionEntity();
            loginedUserSessionEntity.setUserIdx(loginUser.getIdx());
            loginedUserSessionEntity.setUserNickName(loginUser.getNickName());
            session.setAttribute("user", loginedUserSessionEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean signup(String userName, String password, String nickName) {
        try {
            password = sha512_class.hash(password);
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(userName);
            userEntity.setPassword(password);
            userEntity.setNickName(nickName);
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean logout(HttpSession session) {
        try {
            session.removeAttribute("user");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean userinfoUpdate(
                HttpSession session, 
                String newUserName, 
                String newNickName, 
                String newPassword, 
                String password
            ) {
        try {

            LoginedUserSessionEntity loginedUserSessionEntity = (LoginedUserSessionEntity) session.getAttribute("user");
            UserEntity loginedUser = userRepository.findById(loginedUserSessionEntity.getUserIdx()).get();
            String loginedUserPassword = loginedUser.getPassword();
            if (loginedUserPassword != password)
                return false;
            else {
                if (isSet(newNickName))
                    loginedUser.setNickName(newNickName);
                if (isSet(newUserName) && !userRepository.isExistUser(newUserName)) {
                    loginedUser.setUserName(newUserName);
                }
                if (isSet(newPassword))
                    loginedUser.setPassword(newPassword);

                userRepository.save(loginedUser);
                if (isSet(newNickName))
                    loginedUserSessionEntity.setUserNickName(newNickName);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}