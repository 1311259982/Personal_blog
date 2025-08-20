package com.personal.personal_blog.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String slug;
    private Integer views;
    private Boolean isPublished;
    private Date createdAt;
    private Date updatedAt;
}
