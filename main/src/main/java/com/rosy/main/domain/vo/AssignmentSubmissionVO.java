package com.rosy.main.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业提交视图对象
 */
@Data
public class AssignmentSubmissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 作业ID
     */
    private Long assignmentId;

    /**
     * 作业标题
     */
    private String assignmentTitle;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 状态：0-未提交, 1-已提交, 2-已批改
     */
    private Byte status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
