package com.rosy.main.domain.dto.discussion;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 讨论查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiscussionQueryRequest extends PageRequest {

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
     * 发布者ID
     */
    private Long userId;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 是否置顶：0-否, 1-是
     */
    private Byte isTop;
}
