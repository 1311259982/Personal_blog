package com.personal.personal_blog.service;

import com.personal.personal_blog.entity.Comment;

import java.util.List;

public interface CommentsService {
    int post(Comment comment);

    List<Comment> getByPostId(Long postId);
}
