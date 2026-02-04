package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.course.CourseAddRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.dto.course.CourseUpdateRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.service.ICourseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    // region 增删改查

    /**
     * 创建课程
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addCourse(@RequestBody CourseAddRequest courseAddRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(courseAddRequest, course);
        boolean result = courseService.save(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(course.getId());
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteCourse(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = courseService.removeById(id);
        return ApiResponse.success(result);
    }

    /**
     * 更新课程
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest) {
        if (courseUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Course course = BeanUtil.copyProperties(courseUpdateRequest, Course.class);
        boolean result = courseService.updateById(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取课程
     */
    @GetMapping("/get")
    public ApiResponse getCourseById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(course);
    }

    /**
     * 根据 id 获取课程VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getCourseVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(courseService.getCourseVO(course));
    }

    /**
     * 分页获取课程列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listCourseByPage(@RequestBody CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();
        Page<Course> coursePage = courseService.page(new Page<>(current, size),
                courseService.getQueryWrapper(courseQueryRequest));
        return ApiResponse.success(coursePage);
    }

    /**
     * 分页获取课程VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listCourseVOByPage(@RequestBody CourseQueryRequest courseQueryRequest) {
        long current = courseQueryRequest.getCurrent();
        long size = courseQueryRequest.getPageSize();
        Page<Course> coursePage = courseService.page(new Page<>(current, size),
                courseService.getQueryWrapper(courseQueryRequest));
        return ApiResponse.success(courseService.getCourseVOPage(coursePage));
    }

    // endregion

    // region 选课管理

    /**
     * 学生选课
     */
    @PostMapping("/select/{courseId}")
    public ApiResponse selectCourse(@PathVariable Long courseId, @RequestParam Long studentId) {
        if (courseId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = courseService.selectCourse(courseId, studentId);
        return ApiResponse.success(result);
    }

    /**
     * 学生退课
     */
    @PostMapping("/drop/{courseId}")
    public ApiResponse dropCourse(@PathVariable Long courseId, @RequestParam Long studentId) {
        if (courseId == null || studentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = courseService.dropCourse(courseId, studentId);
        return ApiResponse.success(result);
    }

    /**
     * 获取学生已选课程列表
     */
    @GetMapping("/student/{studentId}")
    public ApiResponse getStudentCourses(@PathVariable Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseVO> courses = courseService.getStudentCourses(studentId);
        return ApiResponse.success(courses);
    }

    /**
     * 获取教师教授课程列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ApiResponse getTeacherCourses(@PathVariable Long teacherId) {
        if (teacherId == null || teacherId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseVO> courses = courseService.getTeacherCourses(teacherId);
        return ApiResponse.success(courses);
    }

    // endregion
}
