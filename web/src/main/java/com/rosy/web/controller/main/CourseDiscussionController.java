package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.discussion.DiscussionAddRequest;
import com.rosy.main.domain.dto.discussion.DiscussionQueryRequest;
import com.rosy.main.domain.entity.CourseDiscussion;
import com.rosy.main.domain.vo.CourseDiscussionVO;
import com.rosy.main.service.ICourseDiscussionService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程讨论区表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/discussion")
public class CourseDiscussionController {

    @Resource
    private ICourseDiscussionService discussionService;

    // region 增删改查

    /**
     * 创建讨论
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addDiscussion(@RequestBody DiscussionAddRequest discussionAddRequest) {
        CourseDiscussion discussion = new CourseDiscussion();
        BeanUtils.copyProperties(discussionAddRequest, discussion);
        discussion.setLikeCount(0);
        discussion.setIsTop((byte) 0);
        boolean result = discussionService.save(discussion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(discussion.getId());
    }

    /**
     * 删除讨论
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteDiscussion(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = discussionService.removeById(id);
        return ApiResponse.success(result);
    }

    /**
     * 更新讨论
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateDiscussion(@RequestBody CourseDiscussion discussion) {
        if (discussion.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = discussionService.updateById(discussion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取讨论
     */
    @GetMapping("/get")
    public ApiResponse getDiscussionById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseDiscussion discussion = discussionService.getById(id);
        ThrowUtils.throwIf(discussion == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(discussion);
    }

    /**
     * 根据 id 获取讨论VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getDiscussionVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseDiscussion discussion = discussionService.getById(id);
        ThrowUtils.throwIf(discussion == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(discussionService.getCourseDiscussionVO(discussion));
    }

    /**
     * 分页获取讨论列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listDiscussionByPage(@RequestBody DiscussionQueryRequest discussionQueryRequest) {
        long current = discussionQueryRequest.getCurrent();
        long size = discussionQueryRequest.getPageSize();
        Page<CourseDiscussion> discussionPage = discussionService.page(new Page<>(current, size),
                discussionService.getQueryWrapper(discussionQueryRequest));
        return ApiResponse.success(discussionPage);
    }

    /**
     * 分页获取讨论VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listDiscussionVOByPage(@RequestBody DiscussionQueryRequest discussionQueryRequest) {
        long current = discussionQueryRequest.getCurrent();
        long size = discussionQueryRequest.getPageSize();
        Page<CourseDiscussion> discussionPage = discussionService.page(new Page<>(current, size),
                discussionService.getQueryWrapper(discussionQueryRequest));
        return ApiResponse.success(discussionService.getCourseDiscussionVOPage(discussionPage));
    }

    // endregion

    // region 课程讨论

    /**
     * 获取课程的讨论列表（包含回复）
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse getCourseDiscussions(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseDiscussionVO> discussions = discussionService.getCourseDiscussions(courseId);
        return ApiResponse.success(discussions);
    }

    /**
     * 点赞讨论
     */
    @PostMapping("/like/{id}")
    public ApiResponse likeDiscussion(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = discussionService.likeDiscussion(id);
        return ApiResponse.success(result);
    }

    // endregion
}
