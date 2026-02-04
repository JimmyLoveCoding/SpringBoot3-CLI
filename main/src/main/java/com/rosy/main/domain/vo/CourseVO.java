package com.rosy.main.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程视图对象
 */
@Data
public class CourseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
     * 教师姓名
     */
    private String teacherName;

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
     * 当前学生数
     */
    private Integer currentStudents;

    /**
     * 状态：0-未开课, 1-进行中, 2-已结课
     */
    private Byte status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
