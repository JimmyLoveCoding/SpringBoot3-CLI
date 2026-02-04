package com.rosy.main.domain.dto.attendance;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 请假审批请求
 */
@Data
public class LeaveRequestApproveRequest {

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 审批状态：1-已通过, 2-已拒绝
     */
    @NotNull(message = "审批状态不能为空")
    private Byte status;

    /**
     * 审批备注
     */
    private String approveRemark;

    /**
     * 审批人ID
     */
    @NotNull(message = "审批人ID不能为空")
    private Long approverId;
}
