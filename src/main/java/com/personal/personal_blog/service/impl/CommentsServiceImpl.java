package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.Comment;
import com.personal.personal_blog.mapper.CommentsMapper;
import com.personal.personal_blog.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    public int post(Comment comment) {
        comment.setCreatedAt(new Date());
        comment.setUserId(1L);//后序从JWT中获取userID
         return commentsMapper.insert(comment);
    }

    @Override
    public List<Comment> getByPostId(Long postId) {
        return commentsMapper.selectByPostId(postId);
    }
}
