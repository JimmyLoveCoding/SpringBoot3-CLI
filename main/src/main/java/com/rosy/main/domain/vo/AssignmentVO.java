package com.rosy.main.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业视图对象
 */
@Data
public class AssignmentVO implements Serializable {

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
     * 课程名称
     */
    private String courseName;

    /**
     * 作业标题
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 状态：0-草稿, 1-已发布, 2-已截止
     */
    private Byte status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 提交人数
     */
    private Integer submitCount;

    /**
     * 未提交人数
     */
    private Integer unsubmitCount;

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
