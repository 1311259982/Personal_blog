package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.UserMapper;
import com.personal.personal_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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
    public User login(User user) {
        return userMapper.getUserInfo(user);
    }



}
