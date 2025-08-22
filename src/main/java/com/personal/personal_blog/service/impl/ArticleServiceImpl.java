package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.mapper.ArticleMapper;
import com.personal.personal_blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
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
        for (Post post : postList) {
            post.setIsPublished(true);
            post.setSlug(generateSlug(post.getTitle()));
            post.setCreatedAt(new Date());
            post.setUpdatedAt(new Date());
        }
        articleMapper.insertBatch(postList);
    }

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
        return pinyin.toString().toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
    }


    @Override
    public List<Post> getArticleList(Integer page, Integer size, String sort, Integer categoryId, String tagName) {
        //查询文章列表
        return articleMapper.getArticleList(page, size, sort, categoryId, tagName);
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
        //更新文章并获取受影响的行数，添加更新时间，更新slug
        post.setId(Long.valueOf(id));
        post.setSlug(generateSlug(post.getTitle()));
        post.setUpdatedAt(new Date());


       return articleMapper.updateArticle(post);
        //如果受影响的行数为0，说明文章不存在

    }

    @Override
    public int deleteArticle(Integer id) {
        return articleMapper.deleteArticle(id);
    }
}