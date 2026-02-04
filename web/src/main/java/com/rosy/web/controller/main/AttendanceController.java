package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.attendance.*;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.domain.vo.AttendanceVO;
import com.rosy.main.service.IAttendanceService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 考勤记录表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private IAttendanceService attendanceService;

    // region 增删改查

    /**
     * 创建考勤记录
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addAttendance(@RequestBody AttendanceAddRequest attendanceAddRequest) {
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceAddRequest, attendance);
        boolean result = attendanceService.save(attendance);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(attendance.getId());
    }

    /**
     * 删除考勤记录
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteAttendance(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = attendanceService.removeById(id);
        return ApiResponse.success(result);
    }

    /**
     * 更新考勤记录
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateAttendance(@RequestBody Attendance attendance) {
        if (attendance.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = attendanceService.updateById(attendance);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取考勤记录
     */
    @GetMapping("/get")
    public ApiResponse getAttendanceById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendance = attendanceService.getById(id);
        ThrowUtils.throwIf(attendance == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(attendance);
    }

    /**
     * 根据 id 获取考勤记录VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getAttendanceVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendance = attendanceService.getById(id);
        ThrowUtils.throwIf(attendance == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(attendanceService.getAttendanceVO(attendance));
    }

    /**
     * 分页获取考勤记录列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listAttendanceByPage(@RequestBody AttendanceQueryRequest attendanceQueryRequest) {
        long current = attendanceQueryRequest.getCurrent();
        long size = attendanceQueryRequest.getPageSize();
        Page<Attendance> attendancePage = attendanceService.page(new Page<>(current, size),
                attendanceService.getQueryWrapper(attendanceQueryRequest));
        return ApiResponse.success(attendancePage);
    }

    /**
     * 分页获取考勤记录VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listAttendanceVOByPage(@RequestBody AttendanceQueryRequest attendanceQueryRequest) {
        long current = attendanceQueryRequest.getCurrent();
        long size = attendanceQueryRequest.getPageSize();
        Page<Attendance> attendancePage = attendanceService.page(new Page<>(current, size),
                attendanceService.getQueryWrapper(attendanceQueryRequest));
        return ApiResponse.success(attendanceService.getAttendanceVOPage(attendancePage));
    }

    // endregion

    // region 签到签退

    /**
     * 学生签到
     */
    @PostMapping("/check-in")
    @ValidateRequest
    public ApiResponse checkIn(@RequestBody AttendanceCheckInRequest checkInRequest) {
        Boolean result = attendanceService.checkIn(checkInRequest);
        return ApiResponse.success(result);
    }

    /**
     * 学生签退
     */
    @PostMapping("/check-out")
    public ApiResponse checkOut(@RequestParam Long courseId, @RequestParam Long studentId) {
        if (courseId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = attendanceService.checkOut(courseId, studentId);
        return ApiResponse.success(result);
    }

    // endregion

    // region 统计

    /**
     * 获取学生考勤统计
     */
    @GetMapping("/statistics/student")
    public ApiResponse getStudentStatistics(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        if (studentId == null || courseId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        AttendanceStatisticsVO statistics = attendanceService.getStudentStatistics(studentId, courseId);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取课程所有学生考勤统计
     */
    @GetMapping("/statistics/course/{courseId}")
    public ApiResponse getCourseStatistics(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<AttendanceStatisticsVO> statistics = attendanceService.getCourseStatistics(courseId);
        return ApiResponse.success(statistics);
    }

    /**
     * 批量添加缺勤记录
     */
    @PostMapping("/batch-absent")
    public ApiResponse batchAddAbsent(
            @RequestParam Long courseId,
            @RequestParam List<Long> studentIds,
            @RequestParam String date) {
        if (courseId == null || studentIds == null || date == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = attendanceService.batchAddAbsent(courseId, studentIds, date);
        return ApiResponse.success(result);
    }

    // endregion
}
