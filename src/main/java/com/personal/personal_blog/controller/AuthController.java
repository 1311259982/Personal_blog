package com.personal.personal_blog.controller;


import com.personal.personal_blog.entity.LoginInfo;
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
        try {
            userService.register(user);
            return Result.success();
        } catch (RuntimeException e) {
            // 捕获Service层抛出的异常，并将其信息返回给前端
            return Result.error(e.getMessage());
        }
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        LoginInfo loginInfo = userService.login(user);
        if (loginInfo == null) {
            return Result.error("登录失败，请检查您的用户名/邮箱或密码");
        }
        return Result.success(loginInfo);
    }


}
