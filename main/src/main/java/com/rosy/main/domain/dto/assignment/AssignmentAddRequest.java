package com.rosy.main.domain.dto.assignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业添加请求
 */
@Data
public class AssignmentAddRequest {

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 作业标题
     */
    @NotBlank(message = "作业标题不能为空")
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 附件URL
     */
    private String attachment;

    /**
     * 截止时间
     */
    @NotNull(message = "截止时间不能为空")
    private LocalDateTime deadline;

    /**
     * 总分
     */
    private BigDecimal totalScore;
}
