package com.personal.personal_blog.service;

import com.personal.personal_blog.entity.LoginInfo;
import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.User;

import java.util.List;

public interface UserService {

    void register(User user);

    Boolean checkEmail(String email);

    Boolean checkUsername(String username);

    LoginInfo login(User user);


}
