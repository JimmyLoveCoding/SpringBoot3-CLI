package com.rosy.main.mapper;

import com.rosy.main.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计Mapper
 */
public interface StatisticsMapper {

    /**
     * 获取总学生数
     */
    Integer selectTotalStudents();

    /**
     * 获取总教师数
     */
    Integer selectTotalTeachers();

    /**
     * 获取总课程数
     */
    Integer selectTotalCourses();

    /**
     * 获取总学时
     */
    Integer selectTotalStudyHours();

    /**
     * 获取今日出勤统计
     */
    Map<String, Object> selectTodayAttendanceStatistics(@Param("today") LocalDate today);

    /**
     * 获取待审批请假数
     */
    Integer selectPendingLeaveRequests();

    /**
     * 获取本周出勤趋势
     */
    List<Map<String, Object>> selectWeeklyAttendanceTrend(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 获取课程出勤率排行
     */
    List<Map<String, Object>> selectCourseAttendanceRanking();

    /**
     * 获取教师上课量排行
     */
    List<Map<String, Object>> selectTeacherCourseRanking();

    /**
     * 获取学生考勤统计
     */
    Map<String, Object> selectStudentAttendanceStatistics(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * 获取课程所有学生考勤统计
     */
    List<Map<String, Object>> selectCourseAllStudentsAttendance(@Param("courseId") Long courseId);

    /**
     * 获取教师统计信息
     */
    TeacherStatisticsVO selectTeacherStatistics(@Param("teacherId") Long teacherId);

    /**
     * 获取所有教师统计
     */
    List<TeacherStatisticsVO> selectAllTeacherStatistics();

    /**
     * 获取课程统计信息
     */
    CourseStatisticsVO selectCourseStatistics(@Param("courseId") Long courseId);

    /**
     * 获取所有课程统计
     */
    List<CourseStatisticsVO> selectAllCourseStatistics();
}
