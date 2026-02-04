package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Attendance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface AttendanceMapper extends BaseMapper<Attendance> {

    /**
     * 统计学生考勤情况
     */
    @Select("SELECT " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as attend_count, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as late_count, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) as early_leave_count, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as leave_count, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as absent_count " +
            "FROM attendance " +
            "WHERE student_id = #{studentId} " +
            "AND course_id = #{courseId} " +
            "AND is_deleted = 0")
    Map<String, Object> statisticsByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    /**
     * 统计课程所有学生考勤情况
     */
    @Select("SELECT " +
            "student_id, " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) as attend_count, " +
            "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as late_count, " +
            "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) as early_leave_count, " +
            "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as leave_count, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) as absent_count " +
            "FROM attendance " +
            "WHERE course_id = #{courseId} " +
            "AND is_deleted = 0 " +
            "GROUP BY student_id")
    List<Map<String, Object>> statisticsByCourse(@Param("courseId") Long courseId);

    /**
     * 检查今日是否已签到
     */
    @Select("SELECT COUNT(*) FROM attendance " +
            "WHERE course_id = #{courseId} " +
            "AND student_id = #{studentId} " +
            "AND attend_date = #{attendDate} " +
            "AND is_deleted = 0")
    Integer checkTodayAttendance(@Param("courseId") Long courseId, @Param("studentId") Long studentId, @Param("attendDate") LocalDate attendDate);
}
