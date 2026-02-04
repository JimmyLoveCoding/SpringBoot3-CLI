package com.rosy.main.domain.dto.course;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class CourseUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long id;

    private String courseCode;

    private String courseName;

    private BigDecimal credits;

    private Integer hours;

    private Long teacherId;

    private String classroom;

    private String semester;

    private Byte weekday;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer maxStudents;

    private String description;

    private Byte status;
}
