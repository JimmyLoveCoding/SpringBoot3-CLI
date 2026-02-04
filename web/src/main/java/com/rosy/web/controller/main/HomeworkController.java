package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.homework.HomeworkAddRequest;
import com.rosy.main.domain.dto.homework.HomeworkSubmitRequest;
import com.rosy.main.service.IHomeworkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Resource
    private IHomeworkService homeworkService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse<Long> addHomework(@RequestBody HomeworkAddRequest request) {
        Long homeworkId = homeworkService.addHomework(request);
        return ApiResponse.success(homeworkId);
    }

    @PostMapping("/submit")
    @ValidateRequest
    public ApiResponse<Long> submitHomework(@RequestBody HomeworkSubmitRequest request) {
        Long submissionId = homeworkService.submitHomework(request);
        return ApiResponse.success(submissionId);
    }

    @PostMapping("/grade")
    @ValidateRequest
    public ApiResponse<Boolean> gradeHomework(
            @RequestParam Long submissionId,
            @RequestParam Integer score,
            @RequestParam(required = false) String feedback) {
        Boolean result = homeworkService.gradeHomework(submissionId, score, feedback);
        return ApiResponse.success(result);
    }
}
