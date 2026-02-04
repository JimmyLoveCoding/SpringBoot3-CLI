package com.rosy.main.domain.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 作业批改请求
 */
@Data
public class AssignmentGradeRequest {

    /**
     * 提交ID
     */
    @NotNull(message = "提交ID不能为空")
    private Long id;

    /**
     * 得分
     */
    @NotNull(message = "得分不能为空")
    private BigDecimal score;

    /**
     * 教师反馈
     */
    private String feedback;
}
