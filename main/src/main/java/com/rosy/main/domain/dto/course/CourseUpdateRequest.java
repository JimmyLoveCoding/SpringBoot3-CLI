package com.rosy.main.domain.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程更新请求
 */
@Data
public class CourseUpdateRequest {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 课程编号
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学分
     */
    private BigDecimal credit;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 上课时间
     */
    private String schedule;

    /**
     * 学期
     */
    private String semester;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 最大学生数
     */
    private Integer maxStudents;

    /**
     * 状态：0-未开课, 1-进行中, 2-已结课
     */
    private Byte status;
}
