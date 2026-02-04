package com.rosy.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.material.MaterialQueryRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseMaterial;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.CourseMaterialVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.CourseMaterialMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.ICourseMaterialService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程资料表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@Service
public class CourseMaterialServiceImpl extends ServiceImpl<CourseMaterialMapper, CourseMaterial> implements ICourseMaterialService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public QueryWrapper<CourseMaterial> getQueryWrapper(MaterialQueryRequest materialQueryRequest) {
        QueryWrapper<CourseMaterial> queryWrapper = new QueryWrapper<>();
        if (materialQueryRequest == null) {
            return queryWrapper;
        }

        Long id = materialQueryRequest.getId();
        Long courseId = materialQueryRequest.getCourseId();
        String title = materialQueryRequest.getTitle();
        String fileType = materialQueryRequest.getFileType();
        String sortField = materialQueryRequest.getSortField();
        String sortOrder = materialQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(courseId != null, "course_id", courseId);
        queryWrapper.like(title != null && !title.isEmpty(), "title", title);
        queryWrapper.eq(fileType != null && !fileType.isEmpty(), "file_type", fileType);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public CourseMaterialVO getMaterialVO(CourseMaterial material) {
        if (material == null) {
            return null;
        }
        CourseMaterialVO vo = new CourseMaterialVO();
        BeanUtils.copyProperties(material, vo);

        // 获取课程信息
        Long courseId = material.getCourseId();
        if (courseId != null) {
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }
        }

        // 获取上传者信息
        Long creatorId = material.getCreatorId();
        if (creatorId != null) {
            User creator = userMapper.selectById(creatorId);
            if (creator != null) {
                vo.setCreatorName(creator.getRealName());
            }
        }

        // 格式化文件大小
        vo.setFileSizeText(formatFileSize(material.getFileSize()));

        return vo;
    }

    @Override
    public Page<CourseMaterialVO> getMaterialVOPage(Page<CourseMaterial> materialPage) {
        List<CourseMaterial> materialList = materialPage.getRecords();
        Page<CourseMaterialVO> voPage = new Page<>(materialPage.getCurrent(), materialPage.getSize(), materialPage.getTotal());
        if (CollUtil.isEmpty(materialList)) {
            return voPage;
        }

        Set<Long> courseIdSet = materialList.stream().map(CourseMaterial::getCourseId).collect(Collectors.toSet());
        Map<Long, Course> courseMap = courseMapper.selectBatchIds(courseIdSet).stream()
                .collect(Collectors.toMap(Course::getId, course -> course));

        Set<Long> creatorIdSet = materialList.stream().map(CourseMaterial::getCreatorId).collect(Collectors.toSet());
        Map<Long, User> creatorMap = userMapper.selectBatchIds(creatorIdSet).stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<CourseMaterialVO> voList = materialList.stream().map(material -> {
            CourseMaterialVO vo = new CourseMaterialVO();
            BeanUtils.copyProperties(material, vo);

            Course course = courseMap.get(material.getCourseId());
            if (course != null) {
                vo.setCourseName(course.getCourseName());
            }

            User creator = creatorMap.get(material.getCreatorId());
            if (creator != null) {
                vo.setCreatorName(creator.getRealName());
            }

            vo.setFileSizeText(formatFileSize(material.getFileSize()));

            return vo;
        }).collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<CourseMaterialVO> getCourseMaterials(Long courseId) {
        QueryWrapper<CourseMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.orderByDesc("create_time");
        List<CourseMaterial> materials = this.list(queryWrapper);
        return materials.stream().map(this::getMaterialVO).collect(Collectors.toList());
    }

    @Override
    public Boolean downloadMaterial(Long id) {
        return baseMapper.incrementDownloadCount(id) > 0;
    }

    private String formatFileSize(Long size) {
        if (size == null || size <= 0) {
            return "0 B";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
