package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.course.CourseAddRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.dto.course.CourseUpdateRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.domain.vo.TeacherWorkloadVO;
import com.rosy.main.service.ICourseService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private ICourseService courseService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse<Long> addCourse(@RequestBody CourseAddRequest request) {
        Long courseId = courseService.addCourse(request);
        return ApiResponse.success(courseId);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteCourse(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = courseService.deleteCourse(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse<Boolean> updateCourse(@RequestBody CourseUpdateRequest request) {
        if (request.getId() == null) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR);
        }
        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        boolean result = courseService.updateById(course);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse<Course> getCourseById(@RequestParam long id) {
        if (id <= 0) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR);
        }
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(course);
    }

    @GetMapping("/get/vo")
    public ApiResponse<CourseVO> getCourseVOById(@RequestParam long id) {
        if (id <= 0) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR);
        }
        CourseVO courseVO = courseService.getCourseVOById(id);
        return ApiResponse.success(courseVO);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse<Page<Course>> listCourseByPage(@RequestBody CourseQueryRequest request) {
        Page<Course> page = courseService.getCoursePage(request);
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse<Page<CourseVO>> listCourseVOByPage(@RequestBody CourseQueryRequest request) {
        Page<CourseVO> page = courseService.getCourseVOPage(request);
        return ApiResponse.success(page);
    }

    @GetMapping("/teacher/workload")
    public ApiResponse<List<TeacherWorkloadVO>> getTeacherWorkload(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        List<TeacherWorkloadVO> workloadList = courseService.getTeacherWorkload(teacherId, current, size);
        return ApiResponse.success(workloadList);
    }
}
