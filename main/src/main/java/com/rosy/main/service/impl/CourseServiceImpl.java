package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseStudent;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.CourseVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.CourseStudentMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.ICourseService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseStudentMapper courseStudentMapper;

    @Override
    public QueryWrapper<Course> getQueryWrapper(CourseQueryRequest courseQueryRequest) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (courseQueryRequest == null) {
            return queryWrapper;
        }

        Long id = courseQueryRequest.getId();
        String courseCode = courseQueryRequest.getCourseCode();
        String courseName = courseQueryRequest.getCourseName();
        Long teacherId = courseQueryRequest.getTeacherId();
        String semester = courseQueryRequest.getSemester();
        Byte status = courseQueryRequest.getStatus();
        String searchText = courseQueryRequest.getSearchText();
        String sortField = courseQueryRequest.getSortField();
        String sortOrder = courseQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(courseCode != null && !courseCode.isEmpty(), "course_code", courseCode);
        queryWrapper.like(courseName != null && !courseName.isEmpty(), "course_name", courseName);
        queryWrapper.eq(teacherId != null, "teacher_id", teacherId);
        queryWrapper.eq(semester != null && !semester.isEmpty(), "semester", semester);
        queryWrapper.eq(status != null, "status", status);

        // 搜索关键词
        if (searchText != null && !searchText.isEmpty()) {
            queryWrapper.and(qw -> qw.like("course_name", searchText)
                    .or()
                    .like("course_code", searchText));
        }

        // 排序
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public CourseVO getCourseVO(Course course) {
        if (course == null) {
            return null;
        }
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(course, courseVO);

        // 设置状态文本
        courseVO.setStatusText(getStatusText(course.getStatus()));

        // 获取教师信息
        Long teacherId = course.getTeacherId();
        if (teacherId != null) {
            User teacher = userMapper.selectById(teacherId);
            if (teacher != null) {
                courseVO.setTeacherName(teacher.getRealName());
            }
        }

        // 获取当前学生数
        Integer currentStudents = courseStudentMapper.countByCourseId(course.getId());
        courseVO.setCurrentStudents(currentStudents);

        return courseVO;
    }

    @Override
    public Page<CourseVO> getCourseVOPage(Page<Course> coursePage) {
        List<Course> courseList = coursePage.getRecords();
        Page<CourseVO> courseVOPage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());
        if (CollUtil.isEmpty(courseList)) {
            return courseVOPage;
        }

        // 获取教师信息
        Set<Long> teacherIdSet = courseList.stream().map(Course::getTeacherId).collect(Collectors.toSet());
        Map<Long, User> teacherMap = userMapper.selectBatchIds(teacherIdSet).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 获取学生数
        Map<Long, Integer> studentCountMap = courseList.stream()
                .collect(Collectors.toMap(Course::getId, course -> courseStudentMapper.countByCourseId(course.getId())));

        List<CourseVO> courseVOList = courseList.stream().map(course -> {
            CourseVO courseVO = new CourseVO();
            BeanUtils.copyProperties(course, courseVO);
            courseVO.setStatusText(getStatusText(course.getStatus()));

            User teacher = teacherMap.get(course.getTeacherId());
            if (teacher != null) {
                courseVO.setTeacherName(teacher.getRealName());
            }

            courseVO.setCurrentStudents(studentCountMap.getOrDefault(course.getId(), 0));
            return courseVO;
        }).collect(Collectors.toList());

        courseVOPage.setRecords(courseVOList);
        return courseVOPage;
    }

    @Override
    public Boolean selectCourse(Long courseId, Long studentId) {
        // 检查是否已选课
        QueryWrapper<CourseStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("student_id", studentId);
        if (courseStudentMapper.selectCount(queryWrapper) > 0) {
            return false;
        }

        // 检查课程容量
        Course course = this.getById(courseId);
        if (course == null) {
            return false;
        }
        Integer currentStudents = courseStudentMapper.countByCourseId(courseId);
        if (currentStudents >= course.getMaxStudents()) {
            return false;
        }

        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourseId(courseId);
        courseStudent.setStudentId(studentId);
        return courseStudentMapper.insert(courseStudent) > 0;
    }

    @Override
    public Boolean dropCourse(Long courseId, Long studentId) {
        QueryWrapper<CourseStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("student_id", studentId);
        return courseStudentMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<CourseVO> getStudentCourses(Long studentId) {
        QueryWrapper<CourseStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId);
        List<CourseStudent> courseStudents = courseStudentMapper.selectList(queryWrapper);

        if (CollUtil.isEmpty(courseStudents)) {
            return List.of();
        }

        List<Long> courseIds = courseStudents.stream().map(CourseStudent::getCourseId).collect(Collectors.toList());
        List<Course> courses = this.listByIds(courseIds);

        return courses.stream().map(this::getCourseVO).collect(Collectors.toList());
    }

    @Override
    public List<CourseVO> getTeacherCourses(Long teacherId) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        List<Course> courses = this.list(queryWrapper);
        return courses.stream().map(this::getCourseVO).collect(Collectors.toList());
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "未开课";
            case 1 -> "进行中";
            case 2 -> "已结课";
            default -> "未知";
        };
    }
}
