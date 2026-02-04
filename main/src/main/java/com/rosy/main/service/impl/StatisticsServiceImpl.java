package com.rosy.main.service.impl;

import com.rosy.main.domain.vo.*;
import com.rosy.main.mapper.StatisticsMapper;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public DashboardStatisticsVO getDashboardStatistics() {
        DashboardStatisticsVO vo = new DashboardStatisticsVO();

        // 基础统计
        vo.setTotalStudents(statisticsMapper.selectTotalStudents());
        vo.setTotalTeachers(statisticsMapper.selectTotalTeachers());
        vo.setTotalCourses(statisticsMapper.selectTotalCourses());
        vo.setTotalStudyHours(statisticsMapper.selectTotalStudyHours());

        // 今日出勤统计
        LocalDate today = LocalDate.now();
        Map<String, Object> todayStats = statisticsMapper.selectTodayAttendanceStatistics(today);
        if (todayStats != null) {
            Long totalCount = (Long) todayStats.get("total_count");
            Long presentCount = (Long) todayStats.get("present_count");
            Long checkInCount = (Long) todayStats.get("check_in_count");

            if (totalCount != null && totalCount > 0) {
                BigDecimal rate = BigDecimal.valueOf(presentCount != null ? presentCount : 0)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
                vo.setTodayAttendanceRate(rate);
            } else {
                vo.setTodayAttendanceRate(BigDecimal.ZERO);
            }

            vo.setTodayCheckInCount(checkInCount != null ? checkInCount.intValue() : 0);
        } else {
            vo.setTodayAttendanceRate(BigDecimal.ZERO);
            vo.setTodayCheckInCount(0);
        }

        // 待审批请假数
        vo.setPendingLeaveRequests(statisticsMapper.selectPendingLeaveRequests());

        // 本周出勤趋势
        LocalDate weekStart = today.minusDays(6);
        vo.setWeeklyAttendanceTrend(statisticsMapper.selectWeeklyAttendanceTrend(weekStart, today));

        // 课程出勤率排行
        vo.setCourseAttendanceRanking(statisticsMapper.selectCourseAttendanceRanking());

        // 教师上课量排行
        vo.setTeacherCourseRanking(statisticsMapper.selectTeacherCourseRanking());

        return vo;
    }

    @Override
    public AttendanceStatisticsVO getStudentAttendanceStatistics(Long studentId, Long courseId) {
        Map<String, Object> statistics = statisticsMapper.selectStudentAttendanceStatistics(studentId, courseId);
        AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
        vo.setStudentId(studentId);
        vo.setCourseId(courseId);

        if (statistics != null) {
            Long total = (Long) statistics.get("total");
            Long attendCount = (Long) statistics.get("attend_count");
            Long lateCount = (Long) statistics.get("late_count");
            Long earlyLeaveCount = (Long) statistics.get("early_leave_count");
            Long leaveCount = (Long) statistics.get("leave_count");
            Long absentCount = (Long) statistics.get("absent_count");

            vo.setTotalClasses(total != null ? total.intValue() : 0);
            vo.setAttendCount(attendCount != null ? attendCount.intValue() : 0);
            vo.setLateCount(lateCount != null ? lateCount.intValue() : 0);
            vo.setEarlyLeaveCount(earlyLeaveCount != null ? earlyLeaveCount.intValue() : 0);
            vo.setLeaveCount(leaveCount != null ? leaveCount.intValue() : 0);
            vo.setAbsentCount(absentCount != null ? absentCount.intValue() : 0);

            if (vo.getTotalClasses() > 0) {
                BigDecimal rate = BigDecimal.valueOf(vo.getAttendCount() + vo.getLeaveCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(vo.getTotalClasses()), 2, RoundingMode.HALF_UP);
                vo.setAttendanceRate(rate);
            } else {
                vo.setAttendanceRate(BigDecimal.ZERO);
            }
        }

        return vo;
    }

    @Override
    public List<AttendanceStatisticsVO> getCourseAttendanceStatistics(Long courseId) {
        List<Map<String, Object>> statisticsList = statisticsMapper.selectCourseAllStudentsAttendance(courseId);

        return statisticsList.stream().map(statistics -> {
            AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
            Long studentId = (Long) statistics.get("student_id");
            String studentName = (String) statistics.get("student_name");
            
            vo.setStudentId(studentId);
            vo.setStudentName(studentName);
            vo.setCourseId(courseId);

            Long total = (Long) statistics.get("total");
            Long attendCount = (Long) statistics.get("attend_count");
            Long lateCount = (Long) statistics.get("late_count");
            Long earlyLeaveCount = (Long) statistics.get("early_leave_count");
            Long leaveCount = (Long) statistics.get("leave_count");
            Long absentCount = (Long) statistics.get("absent_count");

            vo.setTotalClasses(total != null ? total.intValue() : 0);
            vo.setAttendCount(attendCount != null ? attendCount.intValue() : 0);
            vo.setLateCount(lateCount != null ? lateCount.intValue() : 0);
            vo.setEarlyLeaveCount(earlyLeaveCount != null ? earlyLeaveCount.intValue() : 0);
            vo.setLeaveCount(leaveCount != null ? leaveCount.intValue() : 0);
            vo.setAbsentCount(absentCount != null ? absentCount.intValue() : 0);

            if (vo.getTotalClasses() > 0) {
                BigDecimal rate = BigDecimal.valueOf(vo.getAttendCount() + vo.getLeaveCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(vo.getTotalClasses()), 2, RoundingMode.HALF_UP);
                vo.setAttendanceRate(rate);
            } else {
                vo.setAttendanceRate(BigDecimal.ZERO);
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public TeacherStatisticsVO getTeacherStatistics(Long teacherId) {
        TeacherStatisticsVO vo = statisticsMapper.selectTeacherStatistics(teacherId);
        if (vo == null) {
            vo = new TeacherStatisticsVO();
            vo.setTeacherId(teacherId);
        }
        return vo;
    }

    @Override
    public List<TeacherStatisticsVO> getAllTeacherStatistics() {
        return statisticsMapper.selectAllTeacherStatistics();
    }

    @Override
    public CourseStatisticsVO getCourseStatistics(Long courseId) {
        CourseStatisticsVO vo = statisticsMapper.selectCourseStatistics(courseId);
        if (vo == null) {
            vo = new CourseStatisticsVO();
            vo.setCourseId(courseId);
        }
        return vo;
    }

    @Override
    public List<CourseStatisticsVO> getAllCourseStatistics() {
        return statisticsMapper.selectAllCourseStatistics();
    }
}
