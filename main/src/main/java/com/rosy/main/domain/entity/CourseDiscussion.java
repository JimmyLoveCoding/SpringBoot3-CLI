package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程讨论区表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@TableName("course_discussion")
public class CourseDiscussion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 父评论ID，用于回复
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 附件URL
     */
    private String attachment;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否置顶：0-否, 1-是
     */
    private Byte isTop;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}
