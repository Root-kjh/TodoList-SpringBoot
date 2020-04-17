package com.drk.todolist.Services.User;

import javax.servlet.http.HttpSession;

import com.drk.todolist.Crypto.sha512;
import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.UserInfoDTO;
import com.drk.todolist.DTO.User.UserJwtDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.JWT.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserServicelmpl implements UserService {

    private sha512 sha512_class=new sha512();
    
    UserJwtDTO userJwtDTO;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    public boolean isSet(Object object){
        return object!=null;
    }

    public boolean userDuplicateCheck(String user_name){
        return userRepository.isExistUser(user_name);
    }
    
    @Override
    public String signin(SigninDTO signinDTO){
        String token = null;
        signinDTO.setPassword(sha512_class.hash(signinDTO.getPassword()));
        try{
            authenticate(signinDTO.getUsername(), signinDTO.getPassword());
            final UserEntity userEntity = userRepository.findByUsername(signinDTO.getUsername());
            userJwtDTO = new UserJwtDTO();
            userJwtDTO.setUserIdx(userEntity.getIdx());
            userJwtDTO.setUserName(userEntity.getUsername());
            userJwtDTO.setUserNickName(userEntity.getNickname());
            token = jwtTokenUtil.generateToken(userJwtDTO);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            return token;
        }
    }

    @Override
    public boolean signup(UserInfoDTO userInfoDTO) {
        try {
            if(userRepository.isExistUser(userInfoDTO.getUserName()))
                return false;
            else{
                userInfoDTO.setPassword(sha512_class.hash(userInfoDTO.getPassword()));
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(userInfoDTO.getUserName());
                userEntity.setPassword(userInfoDTO.getPassword());
                userEntity.setNickname(userInfoDTO.getNickName());
                userRepository.save(userEntity);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String userinfoUpdate(UserJwtDTO userJwtDTO, UserInfoDTO newUserInfoDTO) {
        String token = null;
        try {
            UserEntity loginedUser = userRepository.findByUsername(userJwtDTO.getUserName());
            newUserInfoDTO.setPassword(sha512_class.hash(newUserInfoDTO.getPassword()));
            String loginedUserPassword = loginedUser.getPassword();
            if (loginedUserPassword.equals(newUserInfoDTO.getPassword())){
                if (isSet(newUserInfoDTO.getNickName()))
                    loginedUser.setNickname(newUserInfoDTO.getNickName());
                if (isSet(newUserInfoDTO.getUserName()) && !userRepository.isExistUser(newUserInfoDTO.getUserName())) {
                    loginedUser.setUsername(newUserInfoDTO.getUserName());
                }
                if (isSet(newUserInfoDTO.getPassword()))
                    loginedUser.setPassword(newUserInfoDTO.getPassword());

                userRepository.save(loginedUser);
                if (isSet(newUserInfoDTO.getNickName()))
                    userJwtDTO.setUserNickName(newUserInfoDTO.getNickName());
                if (isSet(newUserInfoDTO.getUserName()))
                    userJwtDTO.setUserName(newUserInfoDTO.getUserName());
                
                token = jwtTokenUtil.generateToken(userJwtDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return token;
        }
    }

    @Override
    public boolean userinfoDelete(UserJwtDTO userJwtDTO, String password) {
        try {
            Long userIdx = userJwtDTO.getUserIdx();
            if (userRepository.deleteByIdxAndPassword(userIdx, password))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    @Override
    public UserEntity getUserInfoByUserName(String userName) throws Exception {
        return userRepository.findByUsername(userName);
    }

    private void authenticate (String username, String password) throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("User Disabled",e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials",e);
        }
    }
}