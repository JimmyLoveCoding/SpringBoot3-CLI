package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.dto.attendance.AttendanceSignRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.service.IAttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean signIn(AttendanceSignRequest request) {
        LambdaQueryWrapper<Attendance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attendance::getCourseId, request.getCourseId())
                .eq(Attendance::getStudentId, request.getStudentId())
                .eq(Attendance::getAttendDate, request.getAttendDate());
        Attendance existingAttendance = this.getOne(queryWrapper);
        
        if (existingAttendance != null) {
            if (existingAttendance.getStatus() == 1) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已签到，请勿重复签到");
            }
            existingAttendance.setStatus((byte) 1);
            existingAttendance.setSignTime(LocalDateTime.now());
            existingAttendance.setSignType(request.getSignType());
            existingAttendance.setRemark(request.getRemark());
            return this.updateById(existingAttendance);
        }
        
        Attendance attendance = new Attendance();
        attendance.setCourseId(request.getCourseId());
        attendance.setStudentId(request.getStudentId());
        attendance.setAttendDate(request.getAttendDate());
        attendance.setStatus((byte) 1);
        attendance.setSignTime(LocalDateTime.now());
        attendance.setSignType(request.getSignType());
        attendance.setRemark(request.getRemark());
        return this.save(attendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAttendanceStatus(Long id, Byte status) {
        Attendance attendance = this.getById(id);
        if (attendance == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "考勤记录不存在");
        }
        attendance.setStatus(status);
        return this.updateById(attendance);
    }

    @Override
    public Page<Attendance> getAttendancePage(AttendanceQueryRequest request) {
        Page<Attendance> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<Attendance> queryWrapper = new LambdaQueryWrapper<>();
        QueryWrapperUtil.addCondition(queryWrapper, request.getCourseId(), Attendance::getCourseId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStudentId(), Attendance::getStudentId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getAttendDate(), Attendance::getAttendDate);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), Attendance::getStatus);
        
        if (request.getStartDate() != null) {
            queryWrapper.ge(Attendance::getAttendDate, request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryWrapper.le(Attendance::getAttendDate, request.getEndDate());
        }
        
        queryWrapper.orderByDesc(Attendance::getAttendDate);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<AttendanceStatisticsVO> getStudentAttendanceStatistics(Long courseId, Long studentId) {
        return baseMapper.selectStudentAttendanceStatistics(courseId, studentId);
    }

    @Override
    public List<AttendanceStatisticsVO> getCourseAttendanceStatistics(Long courseId) {
        return baseMapper.selectCourseAttendanceStatistics(courseId);
    }
}
