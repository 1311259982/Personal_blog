package com.personal.personal_blog.controller;

import com.personal.personal_blog.entity.Comment;
import com.personal.personal_blog.entity.Result;
import com.personal.personal_blog.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;


    @PostMapping
    public Result post(@RequestBody Comment comment) {
        if (commentsService.post(comment) > 0) {
            return Result.success();
        }
        return Result.error("评论失败");
    }

    //GET /by-post/{postId}：获取某篇文章的所有评论
    @GetMapping("/by-post/{postId}")
    public Result getByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentsService.getByPostId(postId);
        if(comments==null || comments.isEmpty()){
            return Result.error("该文章暂无评论");
        }
        return Result.success(comments);
    }
}
