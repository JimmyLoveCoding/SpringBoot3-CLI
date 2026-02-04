package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.dto.assignment.AssignmentGradeRequest;
import com.rosy.main.domain.dto.assignment.AssignmentSubmitRequest;
import com.rosy.main.domain.entity.Assignment;
import com.rosy.main.domain.entity.AssignmentSubmission;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.AssignmentSubmissionVO;
import com.rosy.main.mapper.AssignmentMapper;
import com.rosy.main.mapper.AssignmentSubmissionMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IAssignmentSubmissionService;
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
 * 作业提交表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class AssignmentSubmissionServiceImpl extends ServiceImpl<AssignmentSubmissionMapper, AssignmentSubmission> implements IAssignmentSubmissionService {

    @Resource
    private AssignmentMapper assignmentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public QueryWrapper<AssignmentSubmission> getQueryWrapper(Long assignmentId, Long studentId, Byte status) {
        QueryWrapper<AssignmentSubmission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(assignmentId != null, "assignment_id", assignmentId);
        queryWrapper.eq(studentId != null, "student_id", studentId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    @Override
    public AssignmentSubmissionVO getSubmissionVO(AssignmentSubmission submission) {
        if (submission == null) {
            return null;
        }
        AssignmentSubmissionVO vo = new AssignmentSubmissionVO();
        BeanUtils.copyProperties(submission, vo);

        // 设置状态文本
        vo.setStatusText(getStatusText(submission.getStatus()));

        // 获取作业信息
        Long assignmentId = submission.getAssignmentId();
        if (assignmentId != null) {
            Assignment assignment = assignmentMapper.selectById(assignmentId);
            if (assignment != null) {
                vo.setAssignmentTitle(assignment.getTitle());
            }
        }

        // 获取学生信息
        Long studentId = submission.getStudentId();
        if (studentId != null) {
            User student = userMapper.selectById(studentId);
            if (student != null) {
                vo.setStudentName(student.getRealName());
            }
        }

        return vo;
    }

    @Override
    public Page<AssignmentSubmissionVO> getSubmissionVOPage(Page<AssignmentSubmission> submissionPage) {
        List<AssignmentSubmission> submissionList = submissionPage.getRecords();
        Page<AssignmentSubmissionVO> voPage = new Page<>(submissionPage.getCurrent(), submissionPage.getSize(), submissionPage.getTotal());
        if (CollUtil.isEmpty(submissionList)) {
            return voPage;
        }

        Set<Long> assignmentIdSet = submissionList.stream().map(AssignmentSubmission::getAssignmentId).collect(Collectors.toSet());
        Map<Long, Assignment> assignmentMap = assignmentMapper.selectBatchIds(assignmentIdSet).stream()
                .collect(Collectors.toMap(Assignment::getId, assignment -> assignment));

        Set<Long> studentIdSet = submissionList.stream().map(AssignmentSubmission::getStudentId).collect(Collectors.toSet());
        Map<Long, User> studentMap = userMapper.selectBatchIds(studentIdSet).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<AssignmentSubmissionVO> voList = submissionList.stream().map(submission -> {
            AssignmentSubmissionVO vo = new AssignmentSubmissionVO();
            BeanUtils.copyProperties(submission, vo);
            vo.setStatusText(getStatusText(submission.getStatus()));

            Assignment assignment = assignmentMap.get(submission.getAssignmentId());
            if (assignment != null) {
                vo.setAssignmentTitle(assignment.getTitle());
            }

            User student = studentMap.get(submission.getStudentId());
            if (student != null) {
                vo.setStudentName(student.getRealName());
            }

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public Boolean submitAssignment(AssignmentSubmitRequest submitRequest) {
        // 检查是否已提交
        AssignmentSubmission existing = baseMapper.selectByAssignmentAndStudent(
                submitRequest.getAssignmentId(), submitRequest.getStudentId());

        if (existing != null) {
            // 更新提交
            existing.setContent(submitRequest.getContent());
            existing.setAttachment(submitRequest.getAttachment());
            existing.setSubmitTime(LocalDateTime.now());
            existing.setStatus((byte) 1); // 已提交
            return this.updateById(existing);
        } else {
            // 新增提交
            AssignmentSubmission submission = new AssignmentSubmission();
            submission.setAssignmentId(submitRequest.getAssignmentId());
            submission.setStudentId(submitRequest.getStudentId());
            submission.setContent(submitRequest.getContent());
            submission.setAttachment(submitRequest.getAttachment());
            submission.setSubmitTime(LocalDateTime.now());
            submission.setStatus((byte) 1); // 已提交
            return this.save(submission);
        }
    }

    @Override
    public Boolean gradeSubmission(AssignmentGradeRequest gradeRequest) {
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(gradeRequest.getId());
        submission.setScore(gradeRequest.getScore());
        submission.setFeedback(gradeRequest.getFeedback());
        submission.setStatus((byte) 2); // 已批改
        return this.updateById(submission);
    }

    @Override
    public AssignmentSubmissionVO getStudentSubmission(Long assignmentId, Long studentId) {
        AssignmentSubmission submission = baseMapper.selectByAssignmentAndStudent(assignmentId, studentId);
        return getSubmissionVO(submission);
    }

    @Override
    public List<AssignmentSubmissionVO> getAssignmentSubmissions(Long assignmentId) {
        QueryWrapper<AssignmentSubmission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("assignment_id", assignmentId);
        queryWrapper.orderByDesc("create_time");
        List<AssignmentSubmission> submissions = this.list(queryWrapper);
        return submissions.stream().map(this::getSubmissionVO).collect(Collectors.toList());
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        return switch (status) {
            case 0 -> "未提交";
            case 1 -> "已提交";
            case 2 -> "已批改";
            default -> "未知";
        };
    }
}
