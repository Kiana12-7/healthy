package org.example.api.startup;

import org.example.api.entity.User;
import org.example.api.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 项目启动时初始化超级管理员账号
 */
@Component
public class V001InitAdmin implements CommandLineRunner, Ordered {
    // 执行顺序
    public static final int order = 100;
    private final UserRepository userRepository;

    public V001InitAdmin(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int getOrder() {
        return order;
    }

    /**
     * 项目启动后自动执行
     */
    @Override
    public void run(String @NonNull ... args) {
        // 如果数据库里已经有用户了，就不再初始化
        if (this.userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setName("超级管理员");
        admin.setPhone("13800138000");
        admin.setPassword(new BCryptPasswordEncoder().encode("123456"));

        // 保存到数据库
        this.userRepository.save(admin);
    }
}