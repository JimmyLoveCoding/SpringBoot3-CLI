package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.CourseStudent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 学生选课表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {

    /**
     * 统计课程学生数
     */
    @Select("SELECT COUNT(*) FROM course_student WHERE course_id = #{courseId} AND is_deleted = 0")
    Integer countByCourseId(@Param("courseId") Long courseId);
}
