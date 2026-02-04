package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.assignment.AssignmentGradeRequest;
import com.rosy.main.domain.dto.assignment.AssignmentSubmitRequest;
import com.rosy.main.domain.entity.AssignmentSubmission;
import com.rosy.main.domain.vo.AssignmentSubmissionVO;

import java.util.List;

/**
 * <p>
 * 作业提交表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface IAssignmentSubmissionService extends IService<AssignmentSubmission> {

    /**
     * 获取查询条件
     */
    QueryWrapper<AssignmentSubmission> getQueryWrapper(Long assignmentId, Long studentId, Byte status);

    /**
     * 获取提交记录VO
     */
    AssignmentSubmissionVO getSubmissionVO(AssignmentSubmission submission);

    /**
     * 分页获取提交记录VO
     */
    Page<AssignmentSubmissionVO> getSubmissionVOPage(Page<AssignmentSubmission> submissionPage);

    /**
     * 提交作业
     */
    Boolean submitAssignment(AssignmentSubmitRequest submitRequest);

    /**
     * 批改作业
     */
    Boolean gradeSubmission(AssignmentGradeRequest gradeRequest);

    /**
     * 获取学生的提交记录
     */
    AssignmentSubmissionVO getStudentSubmission(Long assignmentId, Long studentId);

    /**
     * 获取作业的所有提交记录
     */
    List<AssignmentSubmissionVO> getAssignmentSubmissions(Long assignmentId);
}
