package com.personal.personal_blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class LoginInfo extends User {
    private String username;
    private String email;
    private String role;
    private Long id;
    private String token;

    public LoginInfo(Long id, String username, String email, String role, String jwtToken) {
    }
}