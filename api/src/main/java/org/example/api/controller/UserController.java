package org.example.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.api.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 Controller
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("me")
    @JsonView(GetCurrentLoginUserJsonView.class)
    public User getCurrentLoginUser() {
        User user = new User();
        user.setName("admin");
        user.setUsername("admin");
        user.setPhone("1241244134124");
        return user;
    }




    private interface GetCurrentLoginUserJsonView {
    }
}
