package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.attendance.LeaveRequestQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.LeaveRequestVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.LeaveRequestMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.ILeaveRequestService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 请假申请表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest> implements ILeaveRequestService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseMapper courseMapper;

    @Override
    public QueryWrapper<LeaveRequest> getQueryWrapper(LeaveRequestQueryRequest leaveRequestQueryRequest) {
        QueryWrapper<LeaveRequest> queryWrapper = new QueryWrapper<>();
        if (leaveRequestQueryRequest == null) {
            return queryWrapper;
        }

        Long id = leaveRequestQueryRequest.getId();
        Long courseId = leaveRequestQueryRequest.getCourseId();
        Long studentId = leaveRequestQueryRequest.getStudentId();
        Byte leaveType = leaveRequestQueryRequest.getLeaveType();
        Byte status = leaveRequestQueryRequest.getStatus();
        String sortField = leaveRequestQueryRequest.getSortField();
        String sortOrder = leaveRequestQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.eq(studentId != null, "student_id", studentId);
        queryWrapper.eq(leaveType != null, "leave_type", leaveType);
        queryWrapper.eq(status != null, "status", status);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public LeaveRequestVO getLeaveRequestVO(LeaveRequest leaveRequest) {
        if (leaveRequest == null) {
            return null;
        }
        LeaveRequestVO leaveRequestVO = new LeaveRequestVO();
        BeanUtils.copyProperties(leaveRequest, leaveRequestVO);

        leaveRequestVO.setLeaveTypeText(getLeaveTypeText(leaveRequest.getLeaveType()));
        leaveRequestVO.setStatusText(getStatusText(leaveRequest.getStatus()));

        // 获取学生信息
        Long studentId = leaveRequest.getStudentId();
        if (studentId != null) {
            User student = userMapper.selectById(studentId);
            if (student != null) {
                leaveRequestVO.setStudentName(student.getRealName());
            }
        }

        // 获取课程信息
        Long courseId = leaveRequest.getCourseId();
        if (courseId != null) {
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                leaveRequestVO.setCourseName(course.getCourseName());
            }
        }

        // 获取审批人信息
        Long approverId = leaveRequest.getApproverId();
        if (approverId != null) {
            User approver = userMapper.selectById(approverId);
            if (approver != null) {
                leaveRequestVO.setApproverName(approver.getRealName());
            }
        }

        return leaveRequestVO;
    }

    @Override
    public Page<LeaveRequestVO> getLeaveRequestVOPage(Page<LeaveRequest> leaveRequestPage) {
        List<LeaveRequest> leaveRequestList = leaveRequestPage.getRecords();
        Page<LeaveRequestVO> leaveRequestVOPage = new Page<>(leaveRequestPage.getCurrent(), leaveRequestPage.getSize(), leaveRequestPage.getTotal());
        if (CollUtil.isEmpty(leaveRequestList)) {
            return leaveRequestVOPage;
        }

        Set<Long> studentIdSet = leaveRequestList.stream().map(LeaveRequest::getStudentId).collect(Collectors.toSet());
        Map<Long, User> studentMap = userMapper.selectBatchIds(studentIdSet).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        Set<Long> courseIdSet = leaveRequestList.stream().map(LeaveRequest::getCourseId).collect(Collectors.toSet());
        Map<Long, Course> courseMap = courseMapper.selectBatchIds(courseIdSet).stream()
                .collect(Collectors.toMap(Course::getId, course -> course));

        Set<Long> approverIdSet = leaveRequestList.stream()
                .map(LeaveRequest::getApproverId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, User> approverMap = CollUtil.isEmpty(approverIdSet) ? Map.of() :
                userMapper.selectBatchIds(approverIdSet).stream()
                        .collect(Collectors.toMap(User::getId, user -> user));

        List<LeaveRequestVO> leaveRequestVOList = leaveRequestList.stream().map(leaveRequest -> {
            LeaveRequestVO leaveRequestVO = new LeaveRequestVO();
            BeanUtils.copyProperties(leaveRequest, leaveRequestVO);
            leaveRequestVO.setLeaveTypeText(getLeaveTypeText(leaveRequest.getLeaveType()));
            leaveRequestVO.setStatusText(getStatusText(leaveRequest.getStatus()));

            User student = studentMap.get(leaveRequest.getStudentId());
            if (student != null) {
                leaveRequestVO.setStudentName(student.getRealName());
            }

            Course course = courseMap.get(leaveRequest.getCourseId());
            if (course != null) {
                leaveRequestVO.setCourseName(course.getCourseName());
            }

            User approver = approverMap.get(leaveRequest.getApproverId());
            if (approver != null) {
                leaveRequestVO.setApproverName(approver.getRealName());
            }

            return leaveRequestVO;
        }).collect(Collectors.toList());

        leaveRequestVOPage.setRecords(leaveRequestVOList);
        return leaveRequestVOPage;
    }

    @Override
    public Boolean approveLeaveRequest(Long id, Byte status, String approveRemark, Long approverId) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(id);
        leaveRequest.setStatus(status);
        leaveRequest.setApproveRemark(approveRemark);
        leaveRequest.setApproverId(approverId);
        leaveRequest.setApproveTime(LocalDateTime.now());
        return this.updateById(leaveRequest);
    }

    private String getLeaveTypeText(Byte leaveType) {
        if (leaveType == null) {
            return "未知";
        }
        return switch (leaveType) {
            case 1 -> "病假";
            case 2 -> "事假";
            case 3 -> "其他";
            default -> "未知";
        };
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "待审批";
            case 1 -> "已通过";
            case 2 -> "已拒绝";
            default -> "未知";
        };
    }
}
