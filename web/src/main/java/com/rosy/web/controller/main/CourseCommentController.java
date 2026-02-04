package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.comment.CommentAddRequest;
import com.rosy.main.domain.entity.CourseComment;
import com.rosy.main.service.ICourseCommentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CourseCommentController {

    @Resource
    private ICourseCommentService courseCommentService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse<Long> addComment(@RequestBody CommentAddRequest request) {
        Long commentId = courseCommentService.addComment(request);
        return ApiResponse.success(commentId);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse<Boolean> deleteComment(@RequestBody IdRequest idRequest) {
        Boolean result = courseCommentService.deleteComment(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/list/page")
    public ApiResponse<Page<CourseComment>> listCommentsByCourse(
            @RequestParam Long courseId,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseComment> page = courseCommentService.getCommentsByCourse(courseId, current, size);
        return ApiResponse.success(page);
    }

    @PostMapping("/like")
    @ValidateRequest
    public ApiResponse<Boolean> likeComment(@RequestBody IdRequest idRequest) {
        Boolean result = courseCommentService.likeComment(idRequest.getId());
        return ApiResponse.success(result);
    }
}
