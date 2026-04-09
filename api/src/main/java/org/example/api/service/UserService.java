package org.example.api.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.dto.LoggedInUserDTO;
import org.example.api.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    /**
     *
     * 将spring中的用户类型转化为数据库实体类型
     * */
    Optional<User> getCurrentUser();

    /**
     * 获取当前登录用户信息
     * */
    UserDetails getCurrentLoginUserDetails();

    LoggedInUserDTO login(String username, String password, HttpServletRequest request);
}
