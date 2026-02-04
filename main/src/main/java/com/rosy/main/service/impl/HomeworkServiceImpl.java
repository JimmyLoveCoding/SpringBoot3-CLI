package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.homework.HomeworkAddRequest;
import com.rosy.main.domain.dto.homework.HomeworkSubmitRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmission;
import com.rosy.main.domain.vo.HomeworkVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.HomeworkMapper;
import com.rosy.main.mapper.HomeworkSubmissionMapper;
import com.rosy.main.service.IHomeworkService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements IHomeworkService {

    @Resource
    @Lazy
    private HomeworkSubmissionMapper homeworkSubmissionMapper;

    @Resource
    @Lazy
    private CourseMapper courseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addHomework(HomeworkAddRequest request) {
        Homework homework = BeanUtil.copyProperties(request, Homework.class);
        homework.setStatus((byte) 1);
        boolean result = this.save(homework);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加作业失败");
        }
        return homework.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteHomework(Long id) {
        Homework homework = this.getById(id);
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业不存在");
        }
        return this.removeById(id);
    }

    @Override
    public Page<HomeworkVO> getHomeworkPage(Long courseId, int current, int size) {
        Page<Homework> page = new Page<>(current, size);
        LambdaQueryWrapper<Homework> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Homework::getCourseId, courseId);
        queryWrapper.orderByDesc(Homework::getCreateTime);
        
        Page<Homework> homeworkPage = this.page(page, queryWrapper);
        Page<HomeworkVO> voPage = new Page<>(homeworkPage.getCurrent(), homeworkPage.getSize(), homeworkPage.getTotal());
        voPage.setRecords(homeworkPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitHomework(HomeworkSubmitRequest request) {
        Homework homework = this.getById(request.getHomeworkId());
        if (homework == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业不存在");
        }
        
        LambdaQueryWrapper<HomeworkSubmission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HomeworkSubmission::getHomeworkId, request.getHomeworkId())
                .eq(HomeworkSubmission::getStudentId, request.getStudentId());
        HomeworkSubmission existing = homeworkSubmissionMapper.selectOne(queryWrapper);
        if (existing != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已提交该作业，请不要重复提交");
        }
        
        HomeworkSubmission submission = new HomeworkSubmission();
        submission.setHomeworkId(request.getHomeworkId());
        submission.setStudentId(request.getStudentId());
        submission.setContent(request.getContent());
        submission.setAttachment(request.getAttachment());
        submission.setSubmitTime(LocalDateTime.now());
        submission.setStatus((byte) 0);
        int result = homeworkSubmissionMapper.insert(submission);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交作业失败");
        }
        return submission.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean gradeHomework(Long submissionId, Integer score, String comment) {
        HomeworkSubmission submission = homeworkSubmissionMapper.selectById(submissionId);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "作业提交记录不存在");
        }
        if (submission.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该作业已批改");
        }
        submission.setScore(score);
        submission.setComment(comment);
        submission.setGradeTime(LocalDateTime.now());
        submission.setStatus((byte) 1);
        return homeworkSubmissionMapper.updateById(submission) > 0;
    }

    @Override
    public List<HomeworkSubmission> getHomeworkSubmissions(Long homeworkId) {
        LambdaQueryWrapper<HomeworkSubmission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HomeworkSubmission::getHomeworkId, homeworkId);
        queryWrapper.orderByDesc(HomeworkSubmission::getSubmitTime);
        return homeworkSubmissionMapper.selectList(queryWrapper);
    }

    private HomeworkVO convertToVO(Homework homework) {
        HomeworkVO vo = BeanUtil.copyProperties(homework, HomeworkVO.class);
        if (homework.getCourseId() != null) {
            Course course = courseMapper.selectById(homework.getCourseId());
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }
        }
        return vo;
    }
}
