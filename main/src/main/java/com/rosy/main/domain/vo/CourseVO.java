package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class CourseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String courseCode;

    private String courseName;

    private BigDecimal credits;

    private Integer hours;

    private Long teacherId;

    private String teacherName;

    private String classroom;

    private String semester;

    private Byte weekday;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer maxStudents;

    private String description;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
