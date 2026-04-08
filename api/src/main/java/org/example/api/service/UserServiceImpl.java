package org.example.api.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.api.dto.LoggedInUserDTO;
import org.example.api.entity.User;
import org.example.api.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     *
     * @return 当前用户
     */
    @Override
    public Optional<User> getCurrentUser() {
        UserDetails userDetails = this.getCurrentLoginUserDetails();

        if (userDetails instanceof User) {
            return Optional.of((User) userDetails);
        }

        return Optional.empty();
    }

    @Override
    public UserDetails getCurrentLoginUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        if (null == user) {
            throw new RuntimeException("没有登录用户");
        }
        return user;
    }

    @Override
    public LoggedInUserDTO login(String username, String password, HttpServletRequest request) {
        // 根据用户名查用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名不存在"));

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码不正确");
        }

        // 构建用户权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();
         authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 如果有权限可以加

        // 创建 Authentication 对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        // 手动将认证信息存入 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 手动触发 Session 创建
        HttpSession session = request.getSession(true); // true 表示如果没有就创建
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return new LoggedInUserDTO(
                user.getId().toString(),
                user.getUsername()
        );
    }
}
