package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.leave.LeaveAddRequest;
import com.rosy.main.domain.dto.leave.LeaveApproveRequest;
import com.rosy.main.domain.dto.leave.LeaveQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;

public interface ILeaveRequestService extends IService<LeaveRequest> {

    Long submitLeave(LeaveAddRequest request);

    Boolean approveLeave(LeaveApproveRequest request);

    Page<LeaveRequest> getLeavePage(LeaveQueryRequest request);
}
