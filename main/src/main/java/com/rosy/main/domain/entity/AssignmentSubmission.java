package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 作业提交表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Data
@TableName("assignment_submission")
public class AssignmentSubmission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作业ID
     */
    private Long assignmentId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 附件URL
     */
    private String attachment;

    /**
     * 得分
     */
    private BigDecimal score;

    /**
     * 教师反馈
     */
    private String feedback;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 状态：0-未提交, 1-已提交, 2-已批改
     */
    private Byte status;

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
