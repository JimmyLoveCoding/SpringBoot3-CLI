package com.rosy.main.domain.dto.comment;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class CommentAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "用户类型不能为空")
    private Byte userType;

    private Long parentId;

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
