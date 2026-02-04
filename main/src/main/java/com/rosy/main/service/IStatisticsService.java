package com.rosy.main.service;

import com.rosy.main.domain.vo.*;

import java.util.List;

/**
 * 统计服务接口
 */
public interface IStatisticsService {

    /**
     * 获取仪表盘统计数据
     */
    DashboardStatisticsVO getDashboardStatistics();

    /**
     * 获取学生考勤统计
     */
    AttendanceStatisticsVO getStudentAttendanceStatistics(Long studentId, Long courseId);

    /**
     * 获取课程所有学生考勤统计
     */
    List<AttendanceStatisticsVO> getCourseAttendanceStatistics(Long courseId);

    /**
     * 获取教师统计
     */
    TeacherStatisticsVO getTeacherStatistics(Long teacherId);

    /**
     * 获取所有教师统计
     */
    List<TeacherStatisticsVO> getAllTeacherStatistics();

    /**
     * 获取课程统计
     */
    CourseStatisticsVO getCourseStatistics(Long courseId);

    /**
     * 获取所有课程统计
     */
    List<CourseStatisticsVO> getAllCourseStatistics();
}
