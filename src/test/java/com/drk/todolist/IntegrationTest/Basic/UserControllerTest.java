package com.drk.todolist.IntegrationTest.Basic;

import com.drk.todolist.DTO.User.SigninDTO;
import com.drk.todolist.DTO.User.SignupDTO;
import com.drk.todolist.DTO.User.UpdateUserDTO;
import com.drk.todolist.Entitis.UserEntity;
import com.drk.todolist.Repositories.TodoRepository;
import com.drk.todolist.Repositories.UserRepository;
import com.drk.todolist.Services.ToDoList.TodoService;
import com.drk.todolist.Services.User.UserService;
import com.drk.todolist.lib.TestLib;
import com.drk.todolist.Config.IntegrationTest;
import com.drk.todolist.Config.JWT.JwtTokenProvider;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends IntegrationTest {

    private UserEntity testUserEntity;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserControllerTest(UserRepository userRepository, TodoRepository todoRepository, MockMvc mockmMvc,
            UserService userService, TodoService todoService, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        super(userRepository, todoRepository, mockmMvc, userService, todoService);
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void signup() throws Exception {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserName(TestLib.testUser.name);
        signupDTO.setNickName(TestLib.testUser.nickName);
        signupDTO.setPassword(TestLib.testUser.password);
        String response = this.mockMvc.perform(post("/auth/signup")
            .content(TestLib.asJsonString(signupDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

        String  responseMessage = (String) ((JSONObject) this.jsonParser.parse(response)).get("Message");

        UserEntity signupUser = 
            this.userRepository.findByUsername(TestLib.testUser.name);

        assertEquals(responseMessage, "Success");
        assertEquals(signupUser.getUsername(), TestLib.testUser.name);
        assertEquals(signupUser.getNickname(), TestLib.testUser.nickName);
    }

    @Test
    public void signin() throws Exception {
        this.makeTestUser();
        
        SigninDTO signinDTO = new SigninDTO();
        signinDTO.setUserName(TestLib.testUser.name);
        signinDTO.setPassword(TestLib.testUser.password);

        String response = this.mockMvc.perform(post("/auth/signin")
            .content(TestLib.asJsonString(signinDTO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        JSONObject responseJson = (JSONObject) this.jsonParser.parse(response);
        System.out.println((String) responseJson.get("jwt"));
        Authentication authentication = jwtTokenProvider.getAuthentication((String) responseJson.get("jwt"));
        
        assertEquals(responseJson.get("userName"), TestLib.testUser.name);
        assertEquals(responseJson.get("nickName"), TestLib.testUser.nickName);
        assertEquals(((UserEntity) authentication.getPrincipal()).getUsername(), TestLib.testUser.name);

    }

    @Test
    public void getUserInfo() throws Exception {
        testUserEntity = this.makeTestUser();

        String jwt = this.getJwt();

        String response = this.mockMvc.perform(get("/user/"+testUserEntity.getIdx())
                .header(TOKEN_HEADER, jwt))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
        JSONObject responseJson = (JSONObject) this.jsonParser.parse(response);
        
        assertEquals(responseJson.get("userName"), testUserEntity.getUsername());
        assertEquals(responseJson.get("nickName"), testUserEntity.getNickname());
        }

    @Test
    public void updateUserInfo() throws Exception {
        testUserEntity = this.makeTestUser();
        String jwt = this.getJwt();

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setNickName(TestLib.newTestUser.nickName);

        String response = 
            this.mockMvc.perform(put("/user/"+testUserEntity.getIdx())
                .header(TOKEN_HEADER, jwt)
                .content(TestLib.asJsonString(updateUserDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JSONObject responseJson = (JSONObject) this.jsonParser.parse(response);

        assertEquals(responseJson.get("nickName"), TestLib.newTestUser.nickName);
    }

    @Test
    public void deleteUser() throws Exception {
        testUserEntity = this.makeTestUser();
        String jwt = this.getJwt();
        String response = mockMvc.perform(delete("/user/"+testUserEntity.getIdx())
            .header(TOKEN_HEADER, jwt))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        JSONObject responseJson = (JSONObject) this.jsonParser.parse(response);

        assertEquals(responseJson.get("Message"), "Success");
        assertNull(this.userRepository.findByUsername(TestLib.testUser.name));
    }

    @Test
    public void modifyPassword() throws Exception {
        testUserEntity = this.makeTestUser();
        String jwt = this.getJwt();
        String response = this.mockMvc.perform(patch("/user/"+testUserEntity.getIdx())
            .content(String.format("{password: '%s'}", TestLib.newTestUser.password)) 
            .contentType(MediaType.APPLICATION_JSON)
            .header(TOKEN_HEADER, jwt))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JSONObject responseJson = (JSONObject) this.jsonParser.parse(response);
    
        String hashedNewPassword = this.userRepository.findByUsername(TestLib.testUser.name).getPassword();

        assertTrue(passwordEncoder.matches(TestLib.newTestUser.password, hashedNewPassword));
        assertEquals(responseJson.get("Message"), "Success");

    }
}