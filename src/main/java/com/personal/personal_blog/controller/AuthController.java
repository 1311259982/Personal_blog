package com.personal.personal_blog.controller;


import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.Result;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (userService.checkEmail(user.getEmail())) {
            return Result.error("邮箱已被注册");
        }
        userService.register(user);
        return Result.success();
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        User userInfo = userService.login(user);
        if (userInfo == null) {
            return Result.error("登录失败");
        }
        return Result.success(userInfo);
    }


}




