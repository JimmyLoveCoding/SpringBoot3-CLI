package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.homework.HomeworkAddRequest;
import com.rosy.main.domain.dto.homework.HomeworkSubmitRequest;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmission;
import com.rosy.main.domain.vo.HomeworkVO;

import java.util.List;

public interface IHomeworkService extends IService<Homework> {

    Long addHomework(HomeworkAddRequest request);

    Boolean deleteHomework(Long id);

    Page<HomeworkVO> getHomeworkPage(Long courseId, int current, int size);

    Long submitHomework(HomeworkSubmitRequest request);

    Boolean gradeHomework(Long submissionId, Integer score, String comment);

    List<HomeworkSubmission> getHomeworkSubmissions(Long homeworkId);
}
