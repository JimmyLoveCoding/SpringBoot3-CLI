package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AttendanceMapper extends BaseMapper<Attendance> {

    List<AttendanceStatisticsVO> selectStudentAttendanceStatistics(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    List<AttendanceStatisticsVO> selectCourseAttendanceStatistics(@Param("courseId") Long courseId);
}
