package org.example.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class
User {

    private Long id;


    private String name;


    private String phone;


    @JsonView(PasswordJsonView.class)
    private String password;


    public void setUsername(String username) {
        this.setPhone(username);
    }

    public interface PasswordJsonView {
    }

    public interface WeChatUserJsonView {
    }

    public interface TeacherJsonView {
    }

    public interface RolesJsonView {

    }

    public interface AuthoritiesJsonView {
    }

    public interface WechatUsersJsonView {
    }

    public interface WechatUsersSizeJsonView {
    }
}

