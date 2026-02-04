package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.comment.CommentAddRequest;
import com.rosy.main.domain.entity.CourseComment;
import com.rosy.main.mapper.CourseCommentMapper;
import com.rosy.main.service.ICourseCommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseCommentServiceImpl extends ServiceImpl<CourseCommentMapper, CourseComment> implements ICourseCommentService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addComment(CommentAddRequest request) {
        CourseComment comment = new CourseComment();
        BeanUtils.copyProperties(request, comment);
        comment.setLikeCount(0);
        comment.setStatus((byte) 1);
        boolean result = this.save(comment);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加评论失败");
        }
        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteComment(Long id) {
        CourseComment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        return this.removeById(id);
    }

    @Override
    public Page<CourseComment> getCommentsByCourse(Long courseId, int current, int size) {
        Page<CourseComment> page = new Page<>(current, size);
        LambdaQueryWrapper<CourseComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseComment::getCourseId, courseId);
        queryWrapper.orderByDesc(CourseComment::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean likeComment(Long id) {
        CourseComment comment = this.getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评论不存在");
        }
        comment.setLikeCount(comment.getLikeCount() + 1);
        return this.updateById(comment);
    }
}
