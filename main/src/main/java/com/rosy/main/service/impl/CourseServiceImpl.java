package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.course.CourseAddRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.dto.course.CourseUpdateRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.domain.vo.TeacherWorkloadVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ICourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    @Lazy
    private TeacherMapper teacherMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addCourse(CourseAddRequest request) {
        Course course = BeanUtil.copyProperties(request, Course.class);
        boolean result = this.save(course);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加课程失败");
        }
        return course.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCourse(CourseUpdateRequest request) {
        Course existingCourse = this.getById(request.getId());
        if (existingCourse == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        Course course = BeanUtil.copyProperties(request, Course.class);
        return this.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteCourse(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        return this.removeById(id);
    }

    @Override
    public CourseVO getCourseById(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            return null;
        }
        return convertToVO(course);
    }

    @Override
    public Page<CourseVO> getCoursePage(CourseQueryRequest request) {
        Page<Course> page = new Page<>(request.getCurrent(), request.getPageSize());
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        QueryWrapperUtil.addCondition(queryWrapper, request.getCourseCode(), Course::getCourseCode);
        QueryWrapperUtil.addCondition(queryWrapper, request.getCourseName(), Course::getCourseName, true);
        QueryWrapperUtil.addCondition(queryWrapper, request.getTeacherId(), Course::getTeacherId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getSemester(), Course::getSemester);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), Course::getStatus);
        queryWrapper.orderByDesc(Course::getCreateTime);
        
        Page<Course> coursePage = this.page(page, queryWrapper);
        Page<CourseVO> voPage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());
        voPage.setRecords(coursePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public List<CourseVO> getCoursesByTeacher(Long teacherId) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getTeacherId, teacherId);
        queryWrapper.orderByDesc(Course::getCreateTime);
        return this.list(queryWrapper).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherWorkloadVO> getTeacherWorkload(String semester, Long teacherId) {
        return baseMapper.selectTeacherWorkload(semester, teacherId);
    }

    private CourseVO convertToVO(Course course) {
        CourseVO vo = BeanUtil.copyProperties(course, CourseVO.class);
        if (course.getTeacherId() != null) {
            Teacher teacher = teacherMapper.selectById(course.getTeacherId());
            if (teacher != null) {
                vo.setTeacherName(teacher.getName());
            }
        }
        return vo;
    }
}
