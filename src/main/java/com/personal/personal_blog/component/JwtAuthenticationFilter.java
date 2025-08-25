package com.personal.personal_blog.component;

import com.personal.personal_blog.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class JwtAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //登录放行
        if ("/api/auth/login".equals(((HttpServletRequest) servletRequest).getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 除登录外其余请求从请求头中获取token
        log.debug("获取token");
        String token = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        //判断token是否存在，如果不存在，说明没有登录，返回错误信息403
        if (token == null || !token.startsWith("Bearer ")) {//如果token不存在或者不是以Bearer开头，说明没有登录，返回错误信息403
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, "未登录");
            return;
        }
        //如果存在，校验令牌，如果校验失败返回错误信息401
        log.debug("校验token");
        String jwt = token.substring(7);
        try {
            JwtUtil.parseToken(jwt);
            //如果校验成功，将用户信息放入请求头
            servletRequest.setAttribute("user", JwtUtil.parseToken(jwt).getSubject());
            //如果校验成功，将用户角色放入请求头
            servletRequest.setAttribute("role", JwtUtil.parseToken(jwt).get("role").toString());
        } catch (Exception e) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "令牌校验失败");
            return;
        }
        //如果校验成功，放行，继续执行后续过滤器
        filterChain.doFilter(servletRequest, servletResponse);
    }


}
