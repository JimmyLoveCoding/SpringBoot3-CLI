package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.TeacherWorkloadVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    List<TeacherWorkloadVO> selectTeacherWorkload(@Param("semester") String semester, @Param("teacherId") Long teacherId);
}
