package com.personal.personal_blog.service.impl;


import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 声明为 Spring Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 我们使用 email 作为登录的用户名
        User user = userMapper.findByEmail(email);

        if (user == null) {
            // 如果用户不存在，必须抛出此异常
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // 因为我们的 User 实体类已经实现了 UserDetails 接口，所以可以直接返回
        return user;
    }
}
