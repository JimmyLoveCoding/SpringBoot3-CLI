package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.attendance.LeaveRequestAddRequest;
import com.rosy.main.domain.dto.attendance.LeaveRequestApproveRequest;
import com.rosy.main.domain.dto.attendance.LeaveRequestQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.vo.LeaveRequestVO;
import com.rosy.main.service.ILeaveRequestService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 请假申请表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/leave-request")
public class LeaveRequestController {

    @Resource
    private ILeaveRequestService leaveRequestService;

    // region 增删改查

    /**
     * 创建请假申请
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addLeaveRequest(@RequestBody LeaveRequestAddRequest leaveRequestAddRequest) {
        LeaveRequest leaveRequest = new LeaveRequest();
        BeanUtils.copyProperties(leaveRequestAddRequest, leaveRequest);
        leaveRequest.setStatus((byte) 0); // 待审批
        boolean result = leaveRequestService.save(leaveRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(leaveRequest.getId());
    }

    /**
     * 删除请假申请
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteLeaveRequest(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = leaveRequestService.removeById(id);
        return ApiResponse.success(result);
    }

    /**
     * 更新请假申请
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        if (leaveRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = leaveRequestService.updateById(leaveRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取请假申请
     */
    @GetMapping("/get")
    public ApiResponse getLeaveRequestById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LeaveRequest leaveRequest = leaveRequestService.getById(id);
        ThrowUtils.throwIf(leaveRequest == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(leaveRequest);
    }

    /**
     * 根据 id 获取请假申请VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getLeaveRequestVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LeaveRequest leaveRequest = leaveRequestService.getById(id);
        ThrowUtils.throwIf(leaveRequest == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(leaveRequestService.getLeaveRequestVO(leaveRequest));
    }

    /**
     * 分页获取请假申请列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listLeaveRequestByPage(@RequestBody LeaveRequestQueryRequest leaveRequestQueryRequest) {
        long current = leaveRequestQueryRequest.getCurrent();
        long size = leaveRequestQueryRequest.getPageSize();
        Page<LeaveRequest> leaveRequestPage = leaveRequestService.page(new Page<>(current, size),
                leaveRequestService.getQueryWrapper(leaveRequestQueryRequest));
        return ApiResponse.success(leaveRequestPage);
    }

    /**
     * 分页获取请假申请VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listLeaveRequestVOByPage(@RequestBody LeaveRequestQueryRequest leaveRequestQueryRequest) {
        long current = leaveRequestQueryRequest.getCurrent();
        long size = leaveRequestQueryRequest.getPageSize();
        Page<LeaveRequest> leaveRequestPage = leaveRequestService.page(new Page<>(current, size),
                leaveRequestService.getQueryWrapper(leaveRequestQueryRequest));
        return ApiResponse.success(leaveRequestService.getLeaveRequestVOPage(leaveRequestPage));
    }

    // endregion

    // region 审批

    /**
     * 审批请假申请
     */
    @PostMapping("/approve")
    @ValidateRequest
    public ApiResponse approveLeaveRequest(@RequestBody LeaveRequestApproveRequest approveRequest) {
        if (approveRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = leaveRequestService.approveLeaveRequest(
                approveRequest.getId(),
                approveRequest.getStatus(),
                approveRequest.getApproveRemark(),
                approveRequest.getApproverId()
        );
        return ApiResponse.success(result);
    }

    // endregion
}
