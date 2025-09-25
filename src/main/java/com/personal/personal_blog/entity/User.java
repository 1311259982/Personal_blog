package com.personal.personal_blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


@Data
public class User implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Date createdAt;
    private Date updatedAt;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 增加对 role 的 null 和空字符串检查
        if (this.role == null || this.role.isEmpty()) {
            // 如果角色为空，返回一个空的权限列表
            return Collections.emptyList();
        }
        // 返回用户的权限集合。这里我们将 role 字符串转换为权限对象。
        // Spring Security 的权限控制是基于 GrantedAuthority 的。
        // 我们需要给角色字符串加上 "ROLE_" 前缀，这是 Spring Security 的约定。
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        // 返回数据库中存储的加密后的密码
        return this.password;
    }

    @Override
    public String getUsername() {
        // 返回用于登录的用户名，这里我们使用 email
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账户是否未过期，这里我们简单返回 true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账户是否未被锁定，这里我们简单返回 true
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证（密码）是否未过期，这里我们简单返回 true
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账户是否可用，这里我们简单返回 true
        return true;
    }

}
