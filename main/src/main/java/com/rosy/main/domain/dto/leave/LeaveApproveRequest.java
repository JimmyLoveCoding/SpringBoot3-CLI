package com.rosy.main.domain.dto.leave;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class LeaveApproveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "请假记录ID不能为空")
    private Long id;

    @NotNull(message = "审批状态不能为空")
    private Byte status;

    private Long approveUser;

    private String approveComment;
}
