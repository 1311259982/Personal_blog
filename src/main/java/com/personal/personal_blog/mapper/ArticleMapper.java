package com.personal.personal_blog.mapper;

import com.personal.personal_blog.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    // 批量插入文章
    @Insert("<script>" +
            "INSERT INTO post (title, content, slug, user_id, created_at, updated_at, is_published) " +
            "VALUES " +
            "<foreach collection=\'list\' item=\'item\' separator=\',\'>" +
            "(#{item.title}, #{item.content}, #{item.slug}, #{item.userId}, #{item.createdAt}, #{item.updatedAt}, #{item.isPublished})" +
            "</foreach>" +
            "</script>")
    void insertBatch(List<Post> postList);

    // 查询文章列表
    @Select("<script>" +
            "SELECT * FROM post " +
            "WHERE is_published = true " +
            "<if test=\'categoryId != null\'>" +
            "AND category_id = #{categoryId} " +
            "</if>" +
            "<if test=\'tagName != null\'>" +
            "AND tag_name = #{tagName} " +
            "</if>" +
            "ORDER BY " +
            "<if test=\'sort != null\'>" +
            "#{sort} " +
            "</if>" +
            "LIMIT #{page}, #{size}" +
            "</script>")
    List<Post> getArticleList(Integer page, Integer size, String sort, Integer categoryId, String tagName);

    @Select("select id, user_id, title, content, slug, views, is_published, created_at, updated_at from post where id = #{id} ")
    Post getArticle(Integer id);

    @Select("<script>" +
            "SELECT * FROM post WHERE user_id = #{userId} " +
            "<if test='isPublished != null'>" +
            "AND is_published = #{isPublished} " +
            "</if>" +
            "ORDER BY created_at DESC" +
            "</script>")
    List<Post> findArticlesByUserId(@Param("userId") Long userId, @Param("isPublished") Boolean isPublished);

    
    // 更新文章
    @Update("<script>" +
            "update post set title = #{title}, content = #{content}, slug = #{slug}, is_published = #{isPublished}, updated_at = #{updatedAt} where id = #{id}" +
            "</script>")
    int updateArticle(Post post);//返回受影响的行数，表示更新成功或失败

    // 更新发布状态
    @Update("UPDATE post SET is_published = #{isPublished}, updated_at = NOW() WHERE id = #{id}")
    int setPublishedStatus(@Param("id") Integer id, @Param("isPublished") boolean isPublished);

    // 删除文章
    @Delete("delete from post where id = #{id}")
    int deleteArticle(Integer id);
}