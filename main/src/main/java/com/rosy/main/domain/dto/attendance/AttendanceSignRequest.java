package com.rosy.main.domain.dto.attendance;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AttendanceSignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @NotNull(message = "考勤日期不能为空")
    private LocalDate attendDate;

    private Byte signType;

    private String remark;
}
