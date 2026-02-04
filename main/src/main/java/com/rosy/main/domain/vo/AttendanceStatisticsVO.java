package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 考勤统计视图对象
 */
@Data
public class AttendanceStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 总课时
     */
    private Integer totalClasses;

    /**
     * 出勤次数
     */
    private Integer attendCount;

    /**
     * 迟到次数
     */
    private Integer lateCount;

    /**
     * 早退次数
     */
    private Integer earlyLeaveCount;

    /**
     * 请假次数
     */
    private Integer leaveCount;

    /**
     * 缺勤次数
     */
    private Integer absentCount;

    /**
     * 出勤率
     */
    private BigDecimal attendanceRate;
}
