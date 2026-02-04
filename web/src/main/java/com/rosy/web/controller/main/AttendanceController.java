package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.dto.attendance.AttendanceSignRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.service.IAttendanceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private IAttendanceService attendanceService;

    @PostMapping("/sign")
    @ValidateRequest
    public ApiResponse<Long> signAttendance(@RequestBody AttendanceSignRequest request) {
        Long attendanceId = attendanceService.signAttendance(request);
        return ApiResponse.success(attendanceId);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse<Page<Attendance>> listAttendanceByPage(@RequestBody AttendanceQueryRequest request) {
        Page<Attendance> page = attendanceService.getAttendancePage(request);
        return ApiResponse.success(page);
    }

    @GetMapping("/student/statistics")
    public ApiResponse<AttendanceStatisticsVO> getStudentAttendanceStatistics(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long courseId) {
        AttendanceStatisticsVO statistics = attendanceService.getStudentAttendanceStatistics(studentId, courseId);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/course/statistics")
    public ApiResponse<AttendanceStatisticsVO> getCourseAttendanceStatistics(@RequestParam Long courseId) {
        AttendanceStatisticsVO statistics = attendanceService.getCourseAttendanceStatistics(courseId);
        return ApiResponse.success(statistics);
    }
}
