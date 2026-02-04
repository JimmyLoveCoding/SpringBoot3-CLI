package com.rosy.main.domain.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 签到请求
 */
@Data
public class AttendanceCheckInRequest {

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 学生ID
     */
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    /**
     * 签到位置
     */
    private String location;
}
