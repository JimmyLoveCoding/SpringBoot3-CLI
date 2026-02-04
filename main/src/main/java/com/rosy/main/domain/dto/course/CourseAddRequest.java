package com.rosy.main.domain.dto.course;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class CourseAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "课程编码不能为空")
    private String courseCode;

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    @NotNull(message = "学分不能为空")
    private BigDecimal credits;

    private Integer hours;

    @NotNull(message = "授课教师不能为空")
    private Long teacherId;

    private String classroom;

    private String semester;

    private Byte weekday;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer maxStudents;

    private String description;
}
