package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.assignment.AssignmentQueryRequest;
import com.rosy.main.domain.entity.Assignment;
import com.rosy.main.domain.vo.AssignmentVO;

import java.util.List;

/**
 * <p>
 * 作业表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IAssignmentService extends IService<Assignment> {

    /**
     * 获取查询条件
     */
    QueryWrapper<Assignment> getQueryWrapper(AssignmentQueryRequest assignmentQueryRequest);

    /**
     * 获取作业VO
     */
    AssignmentVO getAssignmentVO(Assignment assignment);

    /**
     * 分页获取作业VO
     */
    Page<AssignmentVO> getAssignmentVOPage(Page<Assignment> assignmentPage);

    /**
     * 获取课程的作业列表
     */
    List<AssignmentVO> getCourseAssignments(Long courseId);
}
