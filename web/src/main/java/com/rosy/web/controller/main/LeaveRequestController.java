package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.leave.LeaveAddRequest;
import com.rosy.main.domain.dto.leave.LeaveApproveRequest;
import com.rosy.main.domain.dto.leave.LeaveQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.service.ILeaveRequestService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class LeaveRequestController {

    @Resource
    private ILeaveRequestService leaveRequestService;

    @PostMapping("/submit")
    @ValidateRequest
    public ApiResponse<Long> submitLeave(@RequestBody LeaveAddRequest request) {
        Long leaveId = leaveRequestService.submitLeave(request);
        return ApiResponse.success(leaveId);
    }

    @PostMapping("/approve")
    @ValidateRequest
    public ApiResponse<Boolean> approveLeave(@RequestBody LeaveApproveRequest request) {
        Boolean result = leaveRequestService.approveLeave(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse<Page<LeaveRequest>> listLeaveByPage(@RequestBody LeaveQueryRequest request) {
        Page<LeaveRequest> page = leaveRequestService.getLeavePage(request);
        return ApiResponse.success(page);
    }
}
