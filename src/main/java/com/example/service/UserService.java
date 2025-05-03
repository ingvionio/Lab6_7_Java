package com.example.service;

import com.example.model.User;

import java.util.HashMap;
import java.util.Map;


public class UserService {
    private static final Map<String, User> users = new HashMap<>();

    public boolean registerUser(String username, String password, String email) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.isEmpty() ||
                users.containsKey(username)) {
            return false;
        }
        User newUser = new User(username, password, email);
        users.put(username, newUser);
        return true;
    }

    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }


    static {
        users.put("test", new User("test", "password", "test@example.com"));
    }
}