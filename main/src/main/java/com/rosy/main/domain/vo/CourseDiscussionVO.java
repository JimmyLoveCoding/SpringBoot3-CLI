package com.rosy.main.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程讨论视图对象
 */
@Data
public class CourseDiscussionVO implements Serializable {

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
     * 发布者ID
     */
    private Long userId;

    /**
     * 发布者姓名
     */
    private String userName;

    /**
     * 发布者头像
     */
    private String userAvatar;

    /**
     * 父评论ID
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
     * 回复列表
     */
    private List<CourseDiscussionVO> replies;

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
