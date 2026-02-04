package com.rosy.main.domain.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 考勤记录添加请求
 */
@Data
public class AttendanceAddRequest {

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
     * 考勤日期
     */
    @NotNull(message = "考勤日期不能为空")
    private LocalDate attendDate;

    /**
     * 状态：0-缺勤, 1-出勤, 2-迟到, 3-请假, 4-早退
     */
    @NotNull(message = "考勤状态不能为空")
    private Byte status;

    /**
     * 签到位置
     */
    private String location;

    /**
     * 备注
     */
    private String remark;
}
