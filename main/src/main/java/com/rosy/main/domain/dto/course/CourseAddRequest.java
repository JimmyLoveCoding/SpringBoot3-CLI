package com.rosy.main.domain.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 课程添加请求
 */
@Data
public class CourseAddRequest {

    /**
     * 课程编号
     */
    @NotBlank(message = "课程编号不能为空")
    private String courseCode;

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    /**
     * 学分
     */
    @NotNull(message = "学分不能为空")
    private BigDecimal credit;

    /**
     * 教师ID
     */
    @NotNull(message = "教师ID不能为空")
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
