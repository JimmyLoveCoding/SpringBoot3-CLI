package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 课程统计视图对象
 */
@Data
public class CourseStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程编号
     */
    private String courseCode;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 学生人数
     */
    private Integer studentCount;

    /**
     * 布置作业数
     */
    private Integer assignmentCount;

    /**
     * 资料数
     */
    private Integer materialCount;

    /**
     * 讨论数
     */
    private Integer discussionCount;

    /**
     * 总课时
     */
    private Integer totalClasses;

    /**
     * 平均出勤率
     */
    private BigDecimal averageAttendanceRate;

    /**
     * 平均成绩
     */
    private BigDecimal averageScore;
}
