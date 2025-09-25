package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.LoginInfo;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.UserMapper;
import com.personal.personal_blog.service.UserService;
import com.personal.personal_blog.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(User user) {
        // 1. 校验用户名是否存在
        if (checkUsername(user.getUsername())) {
            throw new RuntimeException("用户名已被注册");
        }
        // 2. 校验邮箱是否存在
        if (checkEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        if (user.getRole() == null || user.getRole().isEmpty()) { // 3. 设置默认角色
            user.setRole("USER"); // 默认为普通用户
        }
        user.setCreatedAt(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);
    }

    @Override
    public Boolean checkEmail(String email) {
        return userMapper.findByEmail(email) != null;
    }

    @Override
    public Boolean checkUsername(String username) {
        return userMapper.findByUsername(username) != null;
    }

    @Override
    public LoginInfo login(User user) {
        User dbUser = null;
        // 1. 优先尝试通过 email 查询用户
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            dbUser = userMapper.findByEmail(user.getEmail());
        }

        // 2. 如果 email 未提供或未找到，则尝试通过 username 查询
        if (dbUser == null && user.getUsername() != null && !user.getUsername().isEmpty()) {
            dbUser = userMapper.findByUsername(user.getUsername());
        }

        if (dbUser == null) {
            return null; // 用户不存在
        }

        // 3. 验证密码
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return null; // 密码错误
        }

        // 4. 密码正确，生成 Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", dbUser.getId());
        claims.put("username", dbUser.getUsername());
        claims.put("role", dbUser.getRole());

        String jwtToken = JwtUtil.generateToken(claims, dbUser.getEmail());

        return new LoginInfo(dbUser.getId(), dbUser.getUsername(), dbUser.getRole(), jwtToken);
    }
}
