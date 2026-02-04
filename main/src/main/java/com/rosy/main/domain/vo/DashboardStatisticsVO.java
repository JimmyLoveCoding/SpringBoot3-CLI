package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘统计数据视图对象
 */
@Data
public class DashboardStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总学生数
     */
    private Integer totalStudents;

    /**
     * 总教师数
     */
    private Integer totalTeachers;

    /**
     * 总课程数
     */
    private Integer totalCourses;

    /**
     * 今日出勤率
     */
    private BigDecimal todayAttendanceRate;

    /**
     * 待审批请假数
     */
    private Integer pendingLeaveRequests;

    /**
     * 今日签到人数
     */
    private Integer todayCheckInCount;

    /**
     * 总学时
     */
    private Integer totalStudyHours;

    /**
     * 本周出勤趋势
     */
    private List<Map<String, Object>> weeklyAttendanceTrend;

    /**
     * 课程出勤率排行
     */
    private List<Map<String, Object>> courseAttendanceRanking;

    /**
     * 教师上课量排行
     */
    private List<Map<String, Object>> teacherCourseRanking;
}
