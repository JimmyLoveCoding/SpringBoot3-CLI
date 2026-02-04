package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.AssignmentSubmission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 作业提交表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface AssignmentSubmissionMapper extends BaseMapper<AssignmentSubmission> {

    /**
     * 根据作业ID和学生ID查询提交记录
     */
    @Select("SELECT * FROM assignment_submission " +
            "WHERE assignment_id = #{assignmentId} " +
            "AND student_id = #{studentId} " +
            "AND is_deleted = 0")
    AssignmentSubmission selectByAssignmentAndStudent(@Param("assignmentId") Long assignmentId, @Param("studentId") Long studentId);
}
