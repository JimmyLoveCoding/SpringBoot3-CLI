package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.attendance.LeaveRequestQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.vo.LeaveRequestVO;

/**
 * <p>
 * 请假申请表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface ILeaveRequestService extends IService<LeaveRequest> {

    /**
     * 获取查询条件
     */
    QueryWrapper<LeaveRequest> getQueryWrapper(LeaveRequestQueryRequest leaveRequestQueryRequest);

    /**
     * 获取请假申请VO
     */
    LeaveRequestVO getLeaveRequestVO(LeaveRequest leaveRequest);

    /**
     * 分页获取请假申请VO
     */
    Page<LeaveRequestVO> getLeaveRequestVOPage(Page<LeaveRequest> leaveRequestPage);

    /**
     * 审批请假申请
     */
    Boolean approveLeaveRequest(Long id, Byte status, String approveRemark, Long approverId);
}
