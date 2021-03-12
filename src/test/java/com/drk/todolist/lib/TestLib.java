package com.drk.todolist.lib;

import com.fasterxml.jackson.databind.ObjectMapper;


public final class TestLib {

    public final class testUser{
        public final static String name = "test";
        public final static String nickName = "testNickName";
        public final static String password = "testpw";    
    } 

    public final class newTestUser{
        public final static String name = "new user";
        public final static String nickName = "new nickname";
        public final static String password = "newpw";    
    }

    public final class testTodo{
        public final static String title = "test todo";
        public final static String context = "test context";
    }

    public final class newTestTodo{
        public final static String title = "new todo";
        public final static String context = "new context";
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}