package com.personal.personal_blog.mapper;

import com.personal.personal_blog.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper {
    // 批量插入文章
    @Insert("<script>" +
            "INSERT INTO post (title, content, slug, user_id, created_at, updated_at, is_published) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.title}, #{item.content}, #{item.slug}, #{item.userId}, #{item.createdAt}, #{item.updatedAt}, #{item.isPublished})" +
            "</foreach>" +
            "</script>")
    void insertBatch(List<Post> postList);

    // 查询文章列表
    @Select("<script>" +
            "SELECT * FROM post " +
            "WHERE is_published = true " +
            "<if test='categoryId != null'>" +
            "AND category_id = #{categoryId} " +
            "</if>" +
            "<if test='tagName != null'>" +
            "AND tag_name = #{tagName} " +
            "</if>" +
            "ORDER BY " +
            "<if test='sort != null'>" +
            "#{sort} " +
            "</if>" +
            "LIMIT #{page}, #{size}" +
            "</script>")
    List<Post> getArticleList(Integer page, Integer size, String sort, Integer categoryId, String tagName);

    @Select("select id, user_id, title, content, slug, views, is_published, created_at, updated_at from post where id = #{id} ")
    Post getArticle(Integer id);
}