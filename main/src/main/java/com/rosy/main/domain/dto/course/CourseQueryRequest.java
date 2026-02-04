package com.rosy.main.domain.dto.course;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 课程查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 课程编号
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 学期
     */
    private String semester;

    /**
     * 状态：0-未开课, 1-进行中, 2-已结课
     */
    private Byte status;

    /**
     * 搜索关键词（课程名称或编号）
     */
    private String searchText;
}
