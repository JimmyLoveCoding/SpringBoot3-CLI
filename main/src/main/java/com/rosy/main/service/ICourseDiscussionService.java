package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.discussion.DiscussionQueryRequest;
import com.rosy.main.domain.entity.CourseDiscussion;
import com.rosy.main.domain.vo.CourseDiscussionVO;

import java.util.List;

/**
 * <p>
 * 课程讨论区表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface ICourseDiscussionService extends IService<CourseDiscussion> {

    /**
     * 获取查询条件
     */
    QueryWrapper<CourseDiscussion> getQueryWrapper(DiscussionQueryRequest discussionQueryRequest);

    /**
     * 获取讨论VO
     */
    CourseDiscussionVO getCourseDiscussionVO(CourseDiscussion discussion);

    /**
     * 分页获取讨论VO
     */
    Page<CourseDiscussionVO> getCourseDiscussionVOPage(Page<CourseDiscussion> discussionPage);

    /**
     * 获取课程的讨论列表（包含回复）
     */
    List<CourseDiscussionVO> getCourseDiscussions(Long courseId);

    /**
     * 点赞
     */
    Boolean likeDiscussion(Long id);
}
