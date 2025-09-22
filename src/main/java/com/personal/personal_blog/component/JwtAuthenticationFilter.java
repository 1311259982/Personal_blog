package com.personal.personal_blog.component;

import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.service.impl.UserDetailsServiceImpl;
import com.personal.personal_blog.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component // 声明为 Spring Bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 如果请求头为空或不是以 "Bearer " 开头，直接放行
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            Claims claims = JwtUtil.parseToken(jwt);
            // 假设你在生成 JWT 时，将用户的 email 存入了 subject
            userEmail = claims.getSubject();

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // **核心改动在这里！**
                // 委托 UserDetailsService 从数据库加载完整的 UserDetails
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                log.info("Attempting to authenticate user: '{}', Authorities found: {}", userEmail, userDetails.getAuthorities());
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Token 解析失败，可以在这里处理异常，例如记录日志
            log.error("JWT token parsing failed: {}", e.getMessage());
            // 但我们不中断过滤器链，让后续的安全机制处理未认证的请求

        }
        // 继续处理请求
        filterChain.doFilter(request, response);
    }
}