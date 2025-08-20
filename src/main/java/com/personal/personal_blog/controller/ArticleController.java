package com.personal.personal_blog.controller;

import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.Result;
import com.personal.personal_blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/posts")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result putArticle(@RequestBody List<Post> postList) {
        articleService.putArticle(postList);
        //响应：201 Created
        return Result.success(201);
    }

    //查询文章列表，分页查询，可选查询参数：?page=0&size=10&sort=createdAt,desc&categoryId=1&tagName=Java
    @GetMapping
    public Result getArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "tagName", required = false) String tagName

    ) {
        //查询文章列表
        List<Post> postList = articleService.getArticleList(page, size, sort, categoryId, tagName);
        //响应：200 OK
        return Result.success(postList);
    }


    //获取单篇文章详情
    @GetMapping("/{id}")
    public Result getArticleDetail(@PathVariable Integer id) {
        //查询文章详情
        Post post = articleService.getArticleDetail(id);
        //如果文章不存在，返回404 Not Found
        if (post == null) {
            return Result.error("文章不存在");
        }
        //响应：200 OK
        return Result.success(post);
    }
}