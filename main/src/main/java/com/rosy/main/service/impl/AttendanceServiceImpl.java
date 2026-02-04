package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.attendance.AttendanceCheckInRequest;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import com.rosy.main.domain.vo.AttendanceVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IAttendanceService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 考勤记录表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseMapper courseMapper;

    @Override
    public QueryWrapper<Attendance> getQueryWrapper(AttendanceQueryRequest attendanceQueryRequest) {
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        if (attendanceQueryRequest == null) {
            return queryWrapper;
        }

        Long id = attendanceQueryRequest.getId();
        Long courseId = attendanceQueryRequest.getCourseId();
        Long studentId = attendanceQueryRequest.getStudentId();
        LocalDate attendDate = attendanceQueryRequest.getAttendDate();
        LocalDate startDate = attendanceQueryRequest.getStartDate();
        LocalDate endDate = attendanceQueryRequest.getEndDate();
        Byte status = attendanceQueryRequest.getStatus();
        String sortField = attendanceQueryRequest.getSortField();
        String sortOrder = attendanceQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.eq(studentId != null, "student_id", studentId);
        queryWrapper.eq(attendDate != null, "attend_date", attendDate);
        queryWrapper.ge(startDate != null, "attend_date", startDate);
        queryWrapper.le(endDate != null, "attend_date", endDate);
        queryWrapper.eq(status != null, "status", status);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public AttendanceVO getAttendanceVO(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        AttendanceVO attendanceVO = new AttendanceVO();
        BeanUtils.copyProperties(attendance, attendanceVO);

        attendanceVO.setStatusText(getStatusText(attendance.getStatus()));

        // 获取学生信息
        Long studentId = attendance.getStudentId();
        if (studentId != null) {
            User student = userMapper.selectById(studentId);
            if (student != null) {
                attendanceVO.setStudentName(student.getRealName());
            }
        }

        // 获取课程信息
        Long courseId = attendance.getCourseId();
        if (courseId != null) {
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                attendanceVO.setCourseName(course.getCourseName());
            }
        }

        return attendanceVO;
    }

    @Override
    public Page<AttendanceVO> getAttendanceVOPage(Page<Attendance> attendancePage) {
        List<Attendance> attendanceList = attendancePage.getRecords();
        Page<AttendanceVO> attendanceVOPage = new Page<>(attendancePage.getCurrent(), attendancePage.getSize(), attendancePage.getTotal());
        if (CollUtil.isEmpty(attendanceList)) {
            return attendanceVOPage;
        }

        Set<Long> studentIdSet = attendanceList.stream().map(Attendance::getStudentId).collect(Collectors.toSet());
        Map<Long, User> studentMap = userMapper.selectBatchIds(studentIdSet).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        Set<Long> courseIdSet = attendanceList.stream().map(Attendance::getCourseId).collect(Collectors.toSet());
        Map<Long, Course> courseMap = courseMapper.selectBatchIds(courseIdSet).stream()
                .collect(Collectors.toMap(Course::getId, course -> course));

        List<AttendanceVO> attendanceVOList = attendanceList.stream().map(attendance -> {
            AttendanceVO attendanceVO = new AttendanceVO();
            BeanUtils.copyProperties(attendance, attendanceVO);
            attendanceVO.setStatusText(getStatusText(attendance.getStatus()));

            User student = studentMap.get(attendance.getStudentId());
            if (student != null) {
                attendanceVO.setStudentName(student.getRealName());
            }

            Course course = courseMap.get(attendance.getCourseId());
            if (course != null) {
                attendanceVO.setCourseName(course.getCourseName());
            }

            return attendanceVO;
        }).collect(Collectors.toList());

        attendanceVOPage.setRecords(attendanceVOList);
        return attendanceVOPage;
    }

    @Override
    public Boolean checkIn(AttendanceCheckInRequest checkInRequest) {
        Long courseId = checkInRequest.getCourseId();
        Long studentId = checkInRequest.getStudentId();
        LocalDate today = LocalDate.now();

        // 检查今日是否已签到
        Integer count = baseMapper.checkTodayAttendance(courseId, studentId, today);
        if (count > 0) {
            // 更新签到记录
            QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId);
            queryWrapper.eq("student_id", studentId);
            queryWrapper.eq("attend_date", today);

            Attendance attendance = new Attendance();
            attendance.setCheckInTime(LocalDateTime.now());
            attendance.setLocation(checkInRequest.getLocation());
            attendance.setStatus((byte) 1); // 出勤
            return this.update(attendance, queryWrapper);
        }

        // 新增签到记录
        Attendance attendance = new Attendance();
        attendance.setCourseId(courseId);
        attendance.setStudentId(studentId);
        attendance.setAttendDate(today);
        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setLocation(checkInRequest.getLocation());
        attendance.setStatus((byte) 1); // 出勤
        return this.save(attendance);
    }

    @Override
    public Boolean checkOut(Long courseId, Long studentId) {
        LocalDate today = LocalDate.now();

        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("student_id", studentId);
        queryWrapper.eq("attend_date", today);

        Attendance attendance = new Attendance();
        attendance.setCheckOutTime(LocalDateTime.now());
        return this.update(attendance, queryWrapper);
    }

    @Override
    public AttendanceStatisticsVO getStudentStatistics(Long studentId, Long courseId) {
        Map<String, Object> statistics = baseMapper.statisticsByStudentAndCourse(studentId, courseId);

        AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
        vo.setStudentId(studentId);
        vo.setCourseId(courseId);

        User student = userMapper.selectById(studentId);
        if (student != null) {
            vo.setStudentName(student.getRealName());
        }

        Course course = courseMapper.selectById(courseId);
        if (course != null) {
            vo.setCourseName(course.getCourseName());
        }

        if (statistics != null) {
            Long total = (Long) statistics.get("total");
            Long attendCount = (Long) statistics.get("attend_count");
            Long lateCount = (Long) statistics.get("late_count");
            Long earlyLeaveCount = (Long) statistics.get("early_leave_count");
            Long leaveCount = (Long) statistics.get("leave_count");
            Long absentCount = (Long) statistics.get("absent_count");

            vo.setTotalClasses(total != null ? total.intValue() : 0);
            vo.setAttendCount(attendCount != null ? attendCount.intValue() : 0);
            vo.setLateCount(lateCount != null ? lateCount.intValue() : 0);
            vo.setEarlyLeaveCount(earlyLeaveCount != null ? earlyLeaveCount.intValue() : 0);
            vo.setLeaveCount(leaveCount != null ? leaveCount.intValue() : 0);
            vo.setAbsentCount(absentCount != null ? absentCount.intValue() : 0);

            // 计算出勤率 = (出勤 + 请假) / 总课时
            if (vo.getTotalClasses() > 0) {
                BigDecimal attendanceRate = BigDecimal.valueOf(vo.getAttendCount() + vo.getLeaveCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(vo.getTotalClasses()), 2, RoundingMode.HALF_UP);
                vo.setAttendanceRate(attendanceRate);
            } else {
                vo.setAttendanceRate(BigDecimal.ZERO);
            }
        }

        return vo;
    }

    @Override
    public List<AttendanceStatisticsVO> getCourseStatistics(Long courseId) {
        List<Map<String, Object>> statisticsList = baseMapper.statisticsByCourse(courseId);

        Course course = courseMapper.selectById(courseId);
        String courseName = course != null ? course.getCourseName() : "";

        return statisticsList.stream().map(statistics -> {
            AttendanceStatisticsVO vo = new AttendanceStatisticsVO();
            Long studentId = (Long) statistics.get("student_id");
            vo.setStudentId(studentId);
            vo.setCourseId(courseId);
            vo.setCourseName(courseName);

            User student = userMapper.selectById(studentId);
            if (student != null) {
                vo.setStudentName(student.getRealName());
            }

            Long total = (Long) statistics.get("total");
            Long attendCount = (Long) statistics.get("attend_count");
            Long lateCount = (Long) statistics.get("late_count");
            Long earlyLeaveCount = (Long) statistics.get("early_leave_count");
            Long leaveCount = (Long) statistics.get("leave_count");
            Long absentCount = (Long) statistics.get("absent_count");

            vo.setTotalClasses(total != null ? total.intValue() : 0);
            vo.setAttendCount(attendCount != null ? attendCount.intValue() : 0);
            vo.setLateCount(lateCount != null ? lateCount.intValue() : 0);
            vo.setEarlyLeaveCount(earlyLeaveCount != null ? earlyLeaveCount.intValue() : 0);
            vo.setLeaveCount(leaveCount != null ? leaveCount.intValue() : 0);
            vo.setAbsentCount(absentCount != null ? absentCount.intValue() : 0);

            if (vo.getTotalClasses() > 0) {
                BigDecimal attendanceRate = BigDecimal.valueOf(vo.getAttendCount() + vo.getLeaveCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(vo.getTotalClasses()), 2, RoundingMode.HALF_UP);
                vo.setAttendanceRate(attendanceRate);
            } else {
                vo.setAttendanceRate(BigDecimal.ZERO);
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAddAbsent(Long courseId, List<Long> studentIds, String date) {
        LocalDate attendDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        for (Long studentId : studentIds) {
            // 检查是否已有记录
            QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId);
            queryWrapper.eq("student_id", studentId);
            queryWrapper.eq("attend_date", attendDate);

            if (this.count(queryWrapper) > 0) {
                continue;
            }

            Attendance attendance = new Attendance();
            attendance.setCourseId(courseId);
            attendance.setStudentId(studentId);
            attendance.setAttendDate(attendDate);
            attendance.setStatus((byte) 0); // 缺勤
            this.save(attendance);
        }
        return true;
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "缺勤";
            case 1 -> "出勤";
            case 2 -> "迟到";
            case 3 -> "请假";
            case 4 -> "早退";
            default -> "未知";
        };
    }
}
