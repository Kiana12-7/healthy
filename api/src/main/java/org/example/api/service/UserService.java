package org.example.api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.dto.LoggedInUserDTO;
import org.example.api.entity.User;

public interface UserService {
    LoggedInUserDTO login(String username, String password, HttpServletRequest request);
}
