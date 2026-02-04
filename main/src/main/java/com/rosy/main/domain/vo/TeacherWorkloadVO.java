package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeacherWorkloadVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long teacherId;

    private String teacherName;

    private String teacherNo;

    private String department;

    private Integer courseCount;

    private Integer totalHours;

    private Integer totalStudents;

    private String semester;
}
