package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.course.CourseAddRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.dto.course.CourseUpdateRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.domain.vo.TeacherWorkloadVO;

import java.util.List;

public interface ICourseService extends IService<Course> {

    Long addCourse(CourseAddRequest request);

    Boolean updateCourse(CourseUpdateRequest request);

    Boolean deleteCourse(Long id);

    CourseVO getCourseById(Long id);

    Page<CourseVO> getCoursePage(CourseQueryRequest request);

    List<CourseVO> getCoursesByTeacher(Long teacherId);

    List<TeacherWorkloadVO> getTeacherWorkload(String semester, Long teacherId);
}
