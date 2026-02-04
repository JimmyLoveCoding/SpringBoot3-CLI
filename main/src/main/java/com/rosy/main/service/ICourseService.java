package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;

import java.util.List;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface ICourseService extends IService<Course> {

    /**
     * 获取查询条件
     */
    QueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest);

    /**
     * 获取课程VO
     */
    CourseVO getCourseVO(Course course);

    /**
     * 分页获取课程VO
     */
    Page<CourseVO> getCourseVOPage(Page<Course> coursePage);

    /**
     * 学生选课
     */
    Boolean selectCourse(Long courseId, Long studentId);

    /**
     * 学生退课
     */
    Boolean dropCourse(Long courseId, Long studentId);

    /**
     * 获取学生已选课程列表
     */
    List<CourseVO> getStudentCourses(Long studentId);

    /**
     * 获取教师教授课程列表
     */
    List<CourseVO> getTeacherCourses(Long teacherId);
}
