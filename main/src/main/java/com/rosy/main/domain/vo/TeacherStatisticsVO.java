package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 教师统计视图对象
 */
@Data
public class TeacherStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教授课程数
     */
    private Integer courseCount;

    /**
     * 学生总数
     */
    private Integer studentCount;

    /**
     * 布置作业数
     */
    private Integer assignmentCount;

    /**
     * 上传资料数
     */
    private Integer materialCount;

    /**
     * 总课时
     */
    private Integer totalHours;

    /**
     * 平均出勤率
     */
    private BigDecimal averageAttendanceRate;
}
