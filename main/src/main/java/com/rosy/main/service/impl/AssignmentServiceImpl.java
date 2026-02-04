package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.assignment.AssignmentQueryRequest;
import com.rosy.main.domain.entity.Assignment;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.AssignmentVO;
import com.rosy.main.mapper.AssignmentMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.service.IAssignmentService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 作业表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements IAssignmentService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public QueryWrapper<Assignment> getQueryWrapper(AssignmentQueryRequest assignmentQueryRequest) {
        QueryWrapper<Assignment> queryWrapper = new QueryWrapper<>();
        if (assignmentQueryRequest == null) {
            return queryWrapper;
        }

        Long id = assignmentQueryRequest.getId();
        Long courseId = assignmentQueryRequest.getCourseId();
        String title = assignmentQueryRequest.getTitle();
        Byte status = assignmentQueryRequest.getStatus();
        String sortField = assignmentQueryRequest.getSortField();
        String sortOrder = assignmentQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.like(title != null && !title.isEmpty(), "title", title);
        queryWrapper.eq(status != null, "status", status);

        // 自动更新已截止的作业状态
        queryWrapper.and(qw -> qw.eq("status", 1).lt("deadline", LocalDateTime.now()))
                .setEntity(new Assignment() {{ setStatus((byte) 2); }});

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public AssignmentVO getAssignmentVO(Assignment assignment) {
        if (assignment == null) {
            return null;
        }
        AssignmentVO vo = new AssignmentVO();
        BeanUtils.copyProperties(assignment, vo);

        // 设置状态文本
        vo.setStatusText(getStatusText(assignment.getStatus()));

        // 获取课程信息
        Long courseId = assignment.getCourseId();
        if (courseId != null) {
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }
        }

        // 获取提交人数
        Integer submitCount = baseMapper.countSubmissions(assignment.getId());
        vo.setSubmitCount(submitCount);

        return vo;
    }

    @Override
    public Page<AssignmentVO> getAssignmentVOPage(Page<Assignment> assignmentPage) {
        List<Assignment> assignmentList = assignmentPage.getRecords();
        Page<AssignmentVO> voPage = new Page<>(assignmentPage.getCurrent(), assignmentPage.getSize(), assignmentPage.getTotal());
        if (CollUtil.isEmpty(assignmentList)) {
            return voPage;
        }

        Set<Long> courseIdSet = assignmentList.stream().map(Assignment::getCourseId).collect(Collectors.toSet());
        Map<Long, Course> courseMap = courseMapper.selectBatchIds(courseIdSet).stream()
                .collect(Collectors.toMap(Course::getId, course -> course));

        List<AssignmentVO> voList = assignmentList.stream().map(assignment -> {
            AssignmentVO vo = new AssignmentVO();
            BeanUtils.copyProperties(assignment, vo);
            vo.setStatusText(getStatusText(assignment.getStatus()));

            Course course = courseMap.get(assignment.getCourseId());
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }

            Integer submitCount = baseMapper.countSubmissions(assignment.getId());
            vo.setSubmitCount(submitCount);

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<AssignmentVO> getCourseAssignments(Long courseId) {
        QueryWrapper<Assignment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.orderByDesc("create_time");
        List<Assignment> assignments = this.list(queryWrapper);
        return assignments.stream().map(this::getAssignmentVO).collect(Collectors.toList());
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "草稿";
            case 1 -> "已发布";
            case 2 -> "已截止";
            default -> "未知";
        };
    }
}
