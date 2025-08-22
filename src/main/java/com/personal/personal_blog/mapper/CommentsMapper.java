package com.personal.personal_blog.mapper;

import com.personal.personal_blog.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface CommentsMapper {
    @Insert("insert into comment(post_id, user_id, content, parent_id, created_at) values (#{postId},#{userId},#{content},#{parentId},#{createdAt})")
    int insert(Comment comment);

    @Select("select * from comment where post_id = #{postId}")
    List<Comment> selectByPostId(Long postId);
}
