package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.material.MaterialQueryRequest;
import com.rosy.main.domain.entity.CourseMaterial;
import com.rosy.main.domain.vo.CourseMaterialVO;

import java.util.List;

/**
 * <p>
 * 课程资料表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface ICourseMaterialService extends IService<CourseMaterial> {

    /**
     * 获取查询条件
     */
    QueryWrapper<CourseMaterial> getQueryWrapper(MaterialQueryRequest materialQueryRequest);

    /**
     * 获取资料VO
     */
    CourseMaterialVO getMaterialVO(CourseMaterial material);

    /**
     * 分页获取资料VO
     */
    Page<CourseMaterialVO> getMaterialVOPage(Page<CourseMaterial> materialPage);

    /**
     * 获取课程的资料列表
     */
    List<CourseMaterialVO> getCourseMaterials(Long courseId);

    /**
     * 下载资料（增加下载次数）
     */
    Boolean downloadMaterial(Long id);
}
