package org.example.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.dto.CurrentUserDTO;
import org.example.api.dto.LoggedInUserDTO;
import org.example.api.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public CurrentUserDTO getCurrentLoginUser() {
        return userService.getCurrentLoginUser();
    }

    @PostMapping("/login")
    public LoggedInUserDTO login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request
    ) {
        return userService.login(username, password, request);
    }

    @PostMapping("/register")
    public LoggedInUserDTO register(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request
    ) {
        return userService.register(username, password, request);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        userService.logout(request);
    }
}
