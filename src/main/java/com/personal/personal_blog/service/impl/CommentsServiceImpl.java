package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.Comment;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.CommentsMapper;
import com.personal.personal_blog.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    public int post(Comment comment) {
        // 从安全上下文中获取当前登录的用户信息
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        comment.setCreatedAt(new Date());
        // 【关键】不再使用硬编码，而是从Token中获取当前用户ID
        comment.setUserId(currentUser.getId());
        return commentsMapper.insert(comment);
    }

    @Override
    public List<Comment> getByPostId(Long postId) {
        return commentsMapper.selectByPostId(postId);
    }
}
