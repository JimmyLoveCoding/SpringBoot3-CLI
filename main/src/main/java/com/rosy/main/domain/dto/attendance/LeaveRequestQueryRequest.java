package com.rosy.main.domain.dto.attendance;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 请假申请查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LeaveRequestQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 请假类型：1-病假, 2-事假, 3-其他
     */
    private Byte leaveType;

    /**
     * 审批状态：0-待审批, 1-已通过, 2-已拒绝
     */
    private Byte status;
}
