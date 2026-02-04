package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.leave.LeaveAddRequest;
import com.rosy.main.domain.dto.leave.LeaveApproveRequest;
import com.rosy.main.domain.dto.leave.LeaveQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.mapper.LeaveRequestMapper;
import com.rosy.main.service.ILeaveRequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest> implements ILeaveRequestService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitLeave(LeaveAddRequest request) {
        LeaveRequest leaveRequest = new LeaveRequest();
        BeanUtils.copyProperties(request, leaveRequest);
        leaveRequest.setStatus((byte) 0);
        boolean result = this.save(leaveRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交请假申请失败");
        }
        return leaveRequest.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean approveLeave(LeaveApproveRequest request) {
        LeaveRequest leaveRequest = this.getById(request.getId());
        if (leaveRequest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "请假记录不存在");
        }
        if (leaveRequest.getStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该请假申请已审批");
        }
        leaveRequest.setStatus(request.getStatus());
        leaveRequest.setApproveUser(request.getApproveUser());
        leaveRequest.setApproveTime(LocalDateTime.now());
        leaveRequest.setApproveComment(request.getApproveComment());
        return this.updateById(leaveRequest);
    }

    @Override
    public Page<LeaveRequest> getLeavePage(LeaveQueryRequest request) {
        Page<LeaveRequest> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<LeaveRequest> queryWrapper = new LambdaQueryWrapper<>();
        QueryWrapperUtil.addCondition(queryWrapper, request.getStudentId(), LeaveRequest::getStudentId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getCourseId(), LeaveRequest::getCourseId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getLeaveType(), LeaveRequest::getLeaveType);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), LeaveRequest::getStatus);
        queryWrapper.orderByDesc(LeaveRequest::getCreateTime);
        return this.page(page, queryWrapper);
    }
}
