package com.rosy.main.domain.dto.assignment;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 作业查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AssignmentQueryRequest extends PageRequest {

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
     * 作业标题
     */
    private String title;

    /**
     * 状态：0-草稿, 1-已发布, 2-已截止
     */
    private Byte status;
}
