package com.rosy.main.domain.dto.attendance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 请假申请添加请求
 */
@Data
public class LeaveRequestAddRequest {

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
     * 请假类型：1-病假, 2-事假, 3-其他
     */
    @NotNull(message = "请假类型不能为空")
    private Byte leaveType;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    /**
     * 请假原因
     */
    @NotBlank(message = "请假原因不能为空")
    private String reason;

    /**
     * 附件URL
     */
    private String attachment;
}
