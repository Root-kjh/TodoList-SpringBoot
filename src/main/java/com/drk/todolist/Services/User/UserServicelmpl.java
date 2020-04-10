package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

import com.drk.todolist.Crypto.sha512;
import com.drk.todolist.DTO.UserSessionDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServicelmpl implements UserService {

    private sha512 sha512_class=new sha512();
    
    UserSessionDTO userSessionDTO;

    @Autowired
    UserRepository userRepository;

    public boolean isSet(Object object){
        return object!=null;
    }

    public boolean userDuplicateCheck(String user_name){
        return userRepository.isExistUser(user_name);
    }
    
    @Override
    public UserSessionDTO getUserSession(HttpSession session) throws Exception{
        return (UserSessionDTO) session.getAttribute("user");
    }

    @Override
    public boolean signin(HttpSession session, String userName, String password) {
        password=sha512_class.hash(password);
        UserEntity loginUser = userRepository.findByUsernameAndPassword(userName, password);
        try{
            userSessionDTO = new UserSessionDTO();
            userSessionDTO.setUserIdx(loginUser.getIdx());
            userSessionDTO.setUserNickName(loginUser.getNickname());
            userSessionDTO.setUserName(loginUser.getUsername());
            session.setAttribute("user",userSessionDTO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean signup(String userName, String password, String nickName) {
        try {
            if(userRepository.isExistUser(userName))
                return false;
            else{
                password = sha512_class.hash(password);
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(userName);
                userEntity.setPassword(password);
                userEntity.setNickname(nickName);
                userRepository.save(userEntity);
                return true;
            }
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
            String password) {
        try {
            userSessionDTO = getUserSession(session);
            UserEntity loginedUser = userRepository.findById(userSessionDTO.getUserIdx()).get();
            String loginedUserPassword = loginedUser.getPassword();
            if (loginedUserPassword != password)
                return false;
            else {
                if (isSet(newNickName))
                    loginedUser.setNickname(newNickName);
                if (isSet(newUserName) && !userRepository.isExistUser(newUserName)) {
                    loginedUser.setUsername(newUserName);
                }
                if (isSet(newPassword))
                    loginedUser.setPassword(newPassword);

                userRepository.save(loginedUser);
                if (isSet(newNickName))
                    userSessionDTO.setUserNickName(newNickName);
                if (isSet(newUserName))
                    userSessionDTO.setUserName(newUserName);
                
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean userinfoDelete(HttpSession session, String password) {
        try {
            UserSessionDTO sessionEntity = (UserSessionDTO) session.getAttribute("user");
            Long userIdx = sessionEntity.getUserIdx();
            if (userRepository.deleteByIdxAndPassword(userIdx, password)){
                session.removeAttribute("user");
                return true;
            }else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}