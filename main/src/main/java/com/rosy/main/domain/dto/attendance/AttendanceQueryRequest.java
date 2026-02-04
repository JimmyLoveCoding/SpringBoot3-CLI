package com.rosy.main.domain.dto.attendance;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDate;

/**
 * 考勤记录查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttendanceQueryRequest extends PageRequest {

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
     * 学生ID
     */
    private Long studentId;

    /**
     * 考勤日期
     */
    private LocalDate attendDate;

    /**
     * 考勤日期开始
     */
    private LocalDate startDate;

    /**
     * 考勤日期结束
     */
    private LocalDate endDate;

    /**
     * 状态：0-缺勤, 1-出勤, 2-迟到, 3-请假, 4-早退
     */
    private Byte status;
}
