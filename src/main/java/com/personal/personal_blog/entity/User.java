package com.personal.personal_blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Date createdAt;
    private Date updatedAt;
    private String token;
}
