package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AttendanceStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long studentId;

    private String studentName;

    private String studentNo;

    private Long courseId;

    private String courseName;

    private Integer totalClasses;

    private Integer attendedCount;

    private Integer lateCount;

    private Integer absentCount;

    private Integer leaveCount;

    private BigDecimal attendanceRate;
}
