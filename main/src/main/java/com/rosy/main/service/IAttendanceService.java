package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.attendance.AttendanceCheckInRequest;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.domain.vo.AttendanceVO;

import java.util.List;

/**
 * <p>
 * 考勤记录表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IAttendanceService extends IService<Attendance> {

    /**
     * 获取查询条件
     */
    QueryWrapper<Attendance> getQueryWrapper(AttendanceQueryRequest attendanceQueryRequest);

    /**
     * 获取考勤记录VO
     */
    AttendanceVO getAttendanceVO(Attendance attendance);

    /**
     * 分页获取考勤记录VO
     */
    Page<AttendanceVO> getAttendanceVOPage(Page<Attendance> attendancePage);

    /**
     * 学生签到
     */
    Boolean checkIn(AttendanceCheckInRequest checkInRequest);

    /**
     * 学生签退
     */
    Boolean checkOut(Long courseId, Long studentId);

    /**
     * 获取学生考勤统计
     */
    AttendanceStatisticsVO getStudentStatistics(Long studentId, Long courseId);

    /**
     * 获取课程所有学生考勤统计
     */
    List<AttendanceStatisticsVO> getCourseStatistics(Long courseId);

    /**
     * 批量添加缺勤记录
     */
    Boolean batchAddAbsent(Long courseId, List<Long> studentIds, String date);
}
