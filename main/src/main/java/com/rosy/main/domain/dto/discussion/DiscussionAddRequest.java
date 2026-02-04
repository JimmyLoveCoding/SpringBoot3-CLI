package com.rosy.main.domain.dto.discussion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 讨论添加请求
 */
@Data
public class DiscussionAddRequest {

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 发布者ID
     */
    @NotNull(message = "发布者ID不能为空")
    private Long userId;

    /**
     * 父评论ID，用于回复
     */
    private Long parentId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 附件URL
     */
    private String attachment;
}
