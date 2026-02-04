package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.dto.attendance.AttendanceSignRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;

import java.util.List;

public interface IAttendanceService extends IService<Attendance> {

    Boolean signIn(AttendanceSignRequest request);

    Boolean updateAttendanceStatus(Long id, Byte status);

    Page<Attendance> getAttendancePage(AttendanceQueryRequest request);

    List<AttendanceStatisticsVO> getStudentAttendanceStatistics(Long courseId, Long studentId);

    List<AttendanceStatisticsVO> getCourseAttendanceStatistics(Long courseId);
}
