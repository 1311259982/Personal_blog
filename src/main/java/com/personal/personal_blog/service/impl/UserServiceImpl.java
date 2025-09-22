package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.LoginInfo;
import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.UserMapper;
import com.personal.personal_blog.service.UserService;
import com.personal.personal_blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    //注入密码编辑器
    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public void register(User user) {
        // 【再次确认】确保这段逻辑存在
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER"); // 默认为普通用户
        }
        // 对密码进行处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);
    }

    @Override
    public Boolean checkEmail(String email) {
        return userMapper.findByEmail(email) != null;
    }

    // src/main/java/com/personal/personal_blog/service/impl/UserServiceImpl.java

    @Override
    public LoginInfo login(User user) {
        // 1. 根据 email 查询用户
        User dbUser = userMapper.findByEmail(user.getEmail());
        if (dbUser == null) {
            return null; // 用户不存在
        }

        // 2. 验证密码 (重要！)
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return null; // 密码错误
        }

        // 3. 密码正确，生成 Token
        Map<String,Object> claims = new HashMap<>();
        claims.put("username", dbUser.getUsername());
        claims.put("role", dbUser.getRole());
        claims.put("id", dbUser.getId());

        // 将 dbUser 的 email 作为 subject 传入
        String jwtToken = JwtUtil.generateToken(claims, dbUser.getEmail());
        //String jwtToken = JwtUtil.generateToken(claims);

        return new LoginInfo(dbUser.getId(), dbUser.getUsername(), dbUser.getRole(), jwtToken);
    }



}
