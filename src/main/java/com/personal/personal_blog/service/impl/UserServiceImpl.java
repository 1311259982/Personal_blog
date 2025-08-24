package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.LoginInfo;
import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.UserMapper;
import com.personal.personal_blog.service.UserService;
import com.personal.personal_blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public void register(User user) {
        userMapper.insertUser(user);

    }

    @Override
    public Boolean checkEmail(String email) {
        return userMapper.findByEmail(email) != null;
    }

    @Override
    public LoginInfo login(User user) {

        User u = userMapper.getUserInfo(user);
        if(u!=null){
            Map<String,Object> claims = new HashMap<>();
            claims.put("username",user.getUsername());
            claims.put("email",user.getEmail());
            claims.put("role",user.getRole());
            claims.put("id",user.getId());
            String jwtToken = JwtUtil.generateToken(claims);// 生成token
            return new LoginInfo(u.getId(),u.getUsername(),u.getEmail(),u.getRole(),jwtToken);
        }
        return null;
    }



}
