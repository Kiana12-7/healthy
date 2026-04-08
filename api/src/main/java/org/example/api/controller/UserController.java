package org.example.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import org.example.api.dto.LoggedInUserDTO;
import org.example.api.entity.User;
import org.example.api.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("me")
    @JsonView(GetCurrentLoginUserJsonView.class)
    public User getCurrentLoginUser() {
        User user = new User();
        user.setName("admin");
        user.setUsername("admin");
        user.setPhone("1241244134124");
        return user;
    }

    @PostMapping("/login")
    public LoggedInUserDTO login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request
    ) {
        return userService.login(username, password, request);
    }

    private interface GetCurrentLoginUserJsonView {
    }
}
