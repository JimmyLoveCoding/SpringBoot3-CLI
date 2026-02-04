package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.*;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private IStatisticsService statisticsService;

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/dashboard")
    public ApiResponse getDashboardStatistics() {
        DashboardStatisticsVO statistics = statisticsService.getDashboardStatistics();
        return ApiResponse.success(statistics);
    }

    /**
     * 获取学生考勤统计
     */
    @GetMapping("/attendance/student")
    public ApiResponse getStudentAttendanceStatistics(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        if (studentId == null || courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AttendanceStatisticsVO statistics = statisticsService.getStudentAttendanceStatistics(studentId, courseId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取课程所有学生考勤统计
     */
    @GetMapping("/attendance/course/{courseId}")
    public ApiResponse getCourseAttendanceStatistics(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AttendanceStatisticsVO> statistics = statisticsService.getCourseAttendanceStatistics(courseId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取教师统计
     */
    @GetMapping("/teacher/{teacherId}")
    public ApiResponse getTeacherStatistics(@PathVariable Long teacherId) {
        if (teacherId == null || teacherId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TeacherStatisticsVO statistics = statisticsService.getTeacherStatistics(teacherId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取所有教师统计
     */
    @GetMapping("/teacher/all")
    public ApiResponse getAllTeacherStatistics() {
        List<TeacherStatisticsVO> statistics = statisticsService.getAllTeacherStatistics();
        return ApiResponse.success(statistics);
    }

    /**
     * 获取课程统计
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse getCourseStatistics(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseStatisticsVO statistics = statisticsService.getCourseStatistics(courseId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取所有课程统计
     */
    @GetMapping("/course/all")
    public ApiResponse getAllCourseStatistics() {
        List<CourseStatisticsVO> statistics = statisticsService.getAllCourseStatistics();
        return ApiResponse.success(statistics);
    }
}
