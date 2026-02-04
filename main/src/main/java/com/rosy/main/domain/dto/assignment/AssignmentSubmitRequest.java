package com.rosy.main.domain.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 作业提交请求
 */
@Data
public class AssignmentSubmitRequest {

    /**
     * 作业ID
     */
    @NotNull(message = "作业ID不能为空")
    private Long assignmentId;

    /**
     * 学生ID
     */
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 附件URL
     */
    private String attachment;
}
