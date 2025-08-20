package com.personal.personal_blog.service;

import com.personal.personal_blog.entity.Post;

import java.util.List;

public interface ArticleService {
    void putArticle(List<Post> postList);

    List<Post> getArticleList(Integer page, Integer size, String sort, Integer categoryId, String tagName);

    Post getArticleDetail(Integer id);

    int updateArticle(Integer id, Post post);
}
