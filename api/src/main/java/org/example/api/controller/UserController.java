package org.example.api.controller;

import org.example.api.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 用户Controller
 *
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("me")
    public User getCurrentLoginUser(Principal principal) {
        User user = new User();
        user.setName("admin");
        user.setUsername("admin");
        user.setPhone("1241244134124");
        return user;
    }


    interface GetCurrentLoginUserJsonView {}
}
