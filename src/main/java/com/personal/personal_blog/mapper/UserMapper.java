package com.personal.personal_blog.mapper;

import com.personal.personal_blog.entity.LoginInfo;
import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserMapper {
    @Select("SELECT id, username, password, email, role, created_at, updated_at FROM user WHERE email = #{email}")
    User findByEmail(String email);

    @Select("SELECT id, username, password, email, role, created_at, updated_at FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO user (username, password, email, role, created_at) VALUES (#{username}, #{password}, #{email}, #{role}, #{createdAt})")
    void insertUser(User user);

//    @Select("SELECT id, username,role FROM user WHERE (username = #{username} OR email = #{email}) AND password = #{password}")
//    LoginInfo getUserInfo(User user);

}
