package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.discussion.DiscussionQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseDiscussion;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.CourseDiscussionVO;
import com.rosy.main.mapper.CourseDiscussionMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.ICourseDiscussionService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程讨论区表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class CourseDiscussionServiceImpl extends ServiceImpl<CourseDiscussionMapper, CourseDiscussion> implements ICourseDiscussionService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseMapper courseMapper;

    @Override
    public QueryWrapper<CourseDiscussion> getQueryWrapper(DiscussionQueryRequest discussionQueryRequest) {
        QueryWrapper<CourseDiscussion> queryWrapper = new QueryWrapper<>();
        if (discussionQueryRequest == null) {
            return queryWrapper;
        }

        Long id = discussionQueryRequest.getId();
        Long courseId = discussionQueryRequest.getCourseId();
        Long userId = discussionQueryRequest.getUserId();
        Long parentId = discussionQueryRequest.getParentId();
        Byte isTop = discussionQueryRequest.getIsTop();
        String sortField = discussionQueryRequest.getSortField();
        String sortOrder = discussionQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(parentId != null, "parent_id", parentId);
        queryWrapper.eq(isTop != null, "is_top", isTop);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public CourseDiscussionVO getCourseDiscussionVO(CourseDiscussion discussion) {
        if (discussion == null) {
            return null;
        }
        CourseDiscussionVO vo = new CourseDiscussionVO();
        BeanUtils.copyProperties(discussion, vo);

        // 获取用户信息
        Long userId = discussion.getUserId();
        if (userId != null) {
            User user = userMapper.selectById(userId);
            if (user != null) {
                vo.setUserName(user.getRealName());
                vo.setUserAvatar(user.getAvatar());
            }
        }

        // 获取课程信息
        Long courseId = discussion.getCourseId();
        if (courseId != null) {
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }
        }

        return vo;
    }

    @Override
    public Page<CourseDiscussionVO> getCourseDiscussionVOPage(Page<CourseDiscussion> discussionPage) {
        List<CourseDiscussion> discussionList = discussionPage.getRecords();
        Page<CourseDiscussionVO> voPage = new Page<>(discussionPage.getCurrent(), discussionPage.getSize(), discussionPage.getTotal());
        if (CollUtil.isEmpty(discussionList)) {
            return voPage;
        }

        Set<Long> userIdSet = discussionList.stream().map(CourseDiscussion::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userMapper.selectBatchIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        Set<Long> courseIdSet = discussionList.stream().map(CourseDiscussion::getCourseId).collect(Collectors.toSet());
        Map<Long, Course> courseMap = courseMapper.selectBatchIds(courseIdSet).stream()
                .collect(Collectors.toMap(Course::getId, course -> course));

        List<CourseDiscussionVO> voList = discussionList.stream().map(discussion -> {
            CourseDiscussionVO vo = new CourseDiscussionVO();
            BeanUtils.copyProperties(discussion, vo);

            User user = userMap.get(discussion.getUserId());
            if (user != null) {
                vo.setUserName(user.getRealName());
                vo.setUserAvatar(user.getAvatar());
            }

            Course course = courseMap.get(discussion.getCourseId());
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<CourseDiscussionVO> getCourseDiscussions(Long courseId) {
        // 获取主评论
        List<CourseDiscussion> mainDiscussions = baseMapper.selectMainDiscussions(courseId);
        if (CollUtil.isEmpty(mainDiscussions)) {
            return List.of();
        }

        // 获取所有用户ID
        Set<Long> userIdSet = mainDiscussions.stream().map(CourseDiscussion::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userMapper.selectBatchIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        // 获取课程信息
        Course course = courseMapper.selectById(courseId);
        String courseName = course != null ? course.getCourseName() : "";

        return mainDiscussions.stream().map(discussion -> {
            CourseDiscussionVO vo = new CourseDiscussionVO();
            BeanUtils.copyProperties(discussion, vo);
            vo.setCourseName(courseName);

            User mainUser = userMap.get(discussion.getUserId());
            if (mainUser != null) {
                vo.setUserName(mainUser.getRealName());
                vo.setUserAvatar(mainUser.getAvatar());
            }

            // 获取回复
            List<CourseDiscussion> replies = baseMapper.selectReplies(discussion.getId());
            if (CollUtil.isNotEmpty(replies)) {
                Set<Long> replyUserIdSet = replies.stream().map(CourseDiscussion::getUserId).collect(Collectors.toSet());
                Map<Long, User> replyUserMap = userMapper.selectBatchIds(replyUserIdSet).stream()
                        .collect(Collectors.toMap(User::getId, u -> u));

                List<CourseDiscussionVO> replyVOList = replies.stream().map(reply -> {
                    CourseDiscussionVO replyVO = new CourseDiscussionVO();
                    BeanUtils.copyProperties(reply, replyVO);
                    replyVO.setCourseName(courseName);

                    User replyUser = replyUserMap.get(reply.getUserId());
                    if (replyUser != null) {
                        replyVO.setUserName(replyUser.getRealName());
                        replyVO.setUserAvatar(replyUser.getAvatar());
                    }

                    return replyVO;
                }).collect(Collectors.toList());

                vo.setReplies(replyVOList);
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean likeDiscussion(Long id) {
        return baseMapper.incrementLikeCount(id) > 0;
    }
}
