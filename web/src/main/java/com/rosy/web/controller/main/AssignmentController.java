package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.assignment.AssignmentAddRequest;
import com.rosy.main.domain.dto.assignment.AssignmentGradeRequest;
import com.rosy.main.domain.dto.assignment.AssignmentQueryRequest;
import com.rosy.main.domain.dto.assignment.AssignmentSubmitRequest;
import com.rosy.main.domain.entity.Assignment;
import com.rosy.main.domain.entity.AssignmentSubmission;
import com.rosy.main.domain.vo.AssignmentSubmissionVO;
import com.rosy.main.domain.vo.AssignmentVO;
import com.rosy.main.service.IAssignmentService;
import com.rosy.main.service.IAssignmentSubmissionService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 作业表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/assignment")
public class AssignmentController {

    @Resource
    private IAssignmentService assignmentService;

    @Resource
    private IAssignmentSubmissionService submissionService;

    // region 作业管理

    /**
     * 创建作业
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addAssignment(@RequestBody AssignmentAddRequest assignmentAddRequest) {
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(assignmentAddRequest, assignment);
        assignment.setStatus((byte) 1); // 已发布
        boolean result = assignmentService.save(assignment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(assignment.getId());
    }

    /**
     * 删除作业
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteAssignment(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = assignmentService.removeById(id);
        return ApiResponse.success(result);
    }

    /**
     * 更新作业
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateAssignment(@RequestBody Assignment assignment) {
        if (assignment.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = assignmentService.updateById(assignment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取作业
     */
    @GetMapping("/get")
    public ApiResponse getAssignmentById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Assignment assignment = assignmentService.getById(id);
        ThrowUtils.throwIf(assignment == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(assignment);
    }

    /**
     * 根据 id 获取作业VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getAssignmentVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Assignment assignment = assignmentService.getById(id);
        ThrowUtils.throwIf(assignment == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(assignmentService.getAssignmentVO(assignment));
    }

    /**
     * 分页获取作业列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listAssignmentByPage(@RequestBody AssignmentQueryRequest assignmentQueryRequest) {
        long current = assignmentQueryRequest.getCurrent();
        long size = assignmentQueryRequest.getPageSize();
        Page<Assignment> assignmentPage = assignmentService.page(new Page<>(current, size),
                assignmentService.getQueryWrapper(assignmentQueryRequest));
        return ApiResponse.success(assignmentPage);
    }

    /**
     * 分页获取作业VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listAssignmentVOByPage(@RequestBody AssignmentQueryRequest assignmentQueryRequest) {
        long current = assignmentQueryRequest.getCurrent();
        long size = assignmentQueryRequest.getPageSize();
        Page<Assignment> assignmentPage = assignmentService.page(new Page<>(current, size),
                assignmentService.getQueryWrapper(assignmentQueryRequest));
        return ApiResponse.success(assignmentService.getAssignmentVOPage(assignmentPage));
    }

    /**
     * 获取课程的作业列表
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse getCourseAssignments(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AssignmentVO> assignments = assignmentService.getCourseAssignments(courseId);
        return ApiResponse.success(assignments);
    }

    // endregion

    // region 作业提交

    /**
     * 提交作业
     */
    @PostMapping("/submit")
    @ValidateRequest
    public ApiResponse submitAssignment(@RequestBody AssignmentSubmitRequest submitRequest) {
        Boolean result = submissionService.submitAssignment(submitRequest);
        return ApiResponse.success(result);
    }

    /**
     * 批改作业
     */
    @PostMapping("/grade")
    @ValidateRequest
    public ApiResponse gradeSubmission(@RequestBody AssignmentGradeRequest gradeRequest) {
        Boolean result = submissionService.gradeSubmission(gradeRequest);
        return ApiResponse.success(result);
    }

    /**
     * 获取学生的提交记录
     */
    @GetMapping("/submission")
    public ApiResponse getStudentSubmission(
            @RequestParam Long assignmentId,
            @RequestParam Long studentId) {
        if (assignmentId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AssignmentSubmissionVO submission = submissionService.getStudentSubmission(assignmentId, studentId);
        return ApiResponse.success(submission);
    }

    /**
     * 获取作业的所有提交记录
     */
    @GetMapping("/submissions/{assignmentId}")
    public ApiResponse getAssignmentSubmissions(@PathVariable Long assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AssignmentSubmissionVO> submissions = submissionService.getAssignmentSubmissions(assignmentId);
        return ApiResponse.success(submissions);
    }

    /**
     * 分页获取提交记录列表
     */
    @PostMapping("/submissions/page")
    public ApiResponse listSubmissionsByPage(
            @RequestParam Long assignmentId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Byte status,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        Page<AssignmentSubmission> submissionPage = submissionService.page(
                new Page<>(current, size),
                submissionService.getQueryWrapper(assignmentId, studentId, status));
        return ApiResponse.success(submissionService.getSubmissionVOPage(submissionPage));
    }

    // endregion
}
