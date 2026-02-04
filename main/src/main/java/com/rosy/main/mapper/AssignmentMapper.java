package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Assignment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 作业表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface AssignmentMapper extends BaseMapper<Assignment> {

    /**
     * 统计作业提交人数
     */
    @Select("SELECT COUNT(*) FROM assignment_submission " +
            "WHERE assignment_id = #{assignmentId} " +
            "AND status IN (1, 2) " +
            "AND is_deleted = 0")
    Integer countSubmissions(@Param("assignmentId") Long assignmentId);
}
