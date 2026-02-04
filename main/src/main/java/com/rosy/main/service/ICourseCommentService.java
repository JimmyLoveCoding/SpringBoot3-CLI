package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.comment.CommentAddRequest;
import com.rosy.main.domain.entity.CourseComment;

public interface ICourseCommentService extends IService<CourseComment> {

    Long addComment(CommentAddRequest request);

    Boolean deleteComment(Long id);

    Page<CourseComment> getCommentsByCourse(Long courseId, int current, int size);

    Boolean likeComment(Long id);
}
