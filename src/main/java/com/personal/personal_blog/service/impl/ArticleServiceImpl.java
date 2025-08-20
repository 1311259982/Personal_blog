package com.personal.personal_blog.service.impl;

import com.personal.personal_blog.entity.Post;
import com.personal.personal_blog.mapper.ArticleMapper;
import com.personal.personal_blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
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
}