package org.example.api.service;

import org.example.api.entity.User;

public interface UserService {
    User login(String username, String password);
}
