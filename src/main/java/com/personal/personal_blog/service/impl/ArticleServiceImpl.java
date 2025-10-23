package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.entity.User;
import com.personal.personal_blog.mapper.ArticleMapper;
import com.personal.personal_blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//import com.personal.personal_blog.exception.ArticleNotFoundException;
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public void putArticle(List<Post> postList) {
        // 从安全上下文中获取当前登录的用户信息
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = currentUser.getId();

        for (Post post : postList) {
            // 强制将文章的userId设置为当前登录用户的id
            post.setUserId(currentUserId);

            if (post.getIsPublished() == null) {
                post.setIsPublished(false); // 默认为草稿
            }
            post.setSlug(generateSlug(post.getTitle()));
            post.setCreatedAt(new Date());
            post.setUpdatedAt(new Date());
        }
        articleMapper.insertBatch(postList);
    }
    // 生成文章的slug
    private String generateSlug(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }
        // 将中文转换为拼音
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        StringBuilder pinyin = new StringBuilder();
        for (char c : title.toCharArray()) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    pinyin.append(pinyinArray[0]);
                } else {
                    pinyin.append(c);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                pinyin.append(c);
            }
        }
        // 生成slug
        String baseSlug = pinyin.toString().toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");

        // 添加一个唯一的后缀来避免冲突
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 6);

        return baseSlug + "-" + uniqueSuffix;
    }


    @Override
    public List<Post> getArticleList(Integer page, Integer size, String sort, Integer categoryId, String tagName) {
        //查询文章列表
        return articleMapper.getArticleList(page, size, sort, categoryId, tagName);
    }

    @Override
    public List<Post> getMyArticles(Boolean isPublished) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = currentUser.getId();
        return articleMapper.findArticlesByUserId(currentUserId, isPublished);
    }


    @Override
    public Post getArticleDetail(Integer id) {
        return articleMapper.getArticle(id);
    }

    @Override
    public int updateArticle(Integer id, Post post)  {
        if (post == null) {
            throw new IllegalArgumentException("文章内容不能为空");
        }

        Post existingPost = articleMapper.getArticle(id);
        if (existingPost == null) {
            // 也可以抛出自定义的 ArticleNotFoundException 异常
            return 0;
        }

        post.setId(Long.valueOf(id));

        // 仅当标题确实发生更改时才更新slug
        if (post.getTitle() != null && !post.getTitle().equals(existingPost.getTitle())) {
            post.setSlug(generateSlug(post.getTitle()));
        } else {
            // 否则，保留旧的slug
            post.setSlug(existingPost.getSlug());
        }

        post.setUpdatedAt(new Date());

       return articleMapper.updateArticle(post);
    }

    @Override
    public int deleteArticle(Integer id) {
        return articleMapper.deleteArticle(id);
    }

    @Override
    public int publishArticle(Integer id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = currentUser.getId();

        Post existingPost = articleMapper.getArticle(id);
        if (existingPost == null) {
            // 抛出异常或返回错误代码，表示文章不存在
            throw new RuntimeException("文章不存在");
        }

        // 检查当前用户是否是文章的作者
        if (!Objects.equals(existingPost.getUserId(), currentUserId)) {
            // 抛出权限不足的异常
            throw new AccessDeniedException("您没有权限发布此文章");
        }

        // 调用Mapper更新发布状态
        return articleMapper.setPublishedStatus(id, true);
    }
}