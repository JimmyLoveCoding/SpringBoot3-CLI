package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.CourseDiscussion;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 课程讨论区表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface CourseDiscussionMapper extends BaseMapper<CourseDiscussion> {

    /**
     * 获取课程的讨论列表（不包括回复）
     */
    @Select("SELECT * FROM course_discussion " +
            "WHERE course_id = #{courseId} " +
            "AND parent_id IS NULL " +
            "AND is_deleted = 0 " +
            "ORDER BY is_top DESC, create_time DESC")
    List<CourseDiscussion> selectMainDiscussions(@Param("courseId") Long courseId);

    /**
     * 获取评论的回复列表
     */
    @Select("SELECT * FROM course_discussion " +
            "WHERE parent_id = #{parentId} " +
            "AND is_deleted = 0 " +
            "ORDER BY create_time ASC")
    List<CourseDiscussion> selectReplies(@Param("parentId") Long parentId);

    /**
     * 点赞
     */
    @Update("UPDATE course_discussion SET like_count = like_count + 1 WHERE id = #{id}")
    Integer incrementLikeCount(@Param("id") Long id);
}
