package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.material.MaterialAddRequest;
import com.rosy.main.domain.dto.material.MaterialQueryRequest;
import com.rosy.main.domain.entity.CourseMaterial;
import com.rosy.main.domain.vo.CourseMaterialVO;
import com.rosy.main.service.ICourseMaterialService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程资料表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/material")
public class CourseMaterialController {

    @Resource
    private ICourseMaterialService materialService;

    // region 增删改查

    /**
     * 上传资料
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addMaterial(@RequestBody MaterialAddRequest materialAddRequest) {
        CourseMaterial material = new CourseMaterial();
        BeanUtils.copyProperties(materialAddRequest, material);
        material.setDownloadCount(0);
        boolean result = materialService.save(material);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(material.getId());
    }

    /**
     * 删除资料
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteMaterial(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialService.removeById(id);
        return ApiResponse.success(result);
    }

    /**
     * 更新资料
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateMaterial(@RequestBody CourseMaterial material) {
        if (material.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = materialService.updateById(material);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取资料
     */
    @GetMapping("/get")
    public ApiResponse getMaterialById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseMaterial material = materialService.getById(id);
        ThrowUtils.throwIf(material == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(material);
    }

    /**
     * 根据 id 获取资料VO
     */
    @GetMapping("/get/vo")
    public ApiResponse getMaterialVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        CourseMaterial material = materialService.getById(id);
        ThrowUtils.throwIf(material == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(materialService.getMaterialVO(material));
    }

    /**
     * 分页获取资料列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listMaterialByPage(@RequestBody MaterialQueryRequest materialQueryRequest) {
        long current = materialQueryRequest.getCurrent();
        long size = materialQueryRequest.getPageSize();
        Page<CourseMaterial> materialPage = materialService.page(new Page<>(current, size),
                materialService.getQueryWrapper(materialQueryRequest));
        return ApiResponse.success(materialPage);
    }

    /**
     * 分页获取资料VO列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listMaterialVOByPage(@RequestBody MaterialQueryRequest materialQueryRequest) {
        long current = materialQueryRequest.getCurrent();
        long size = materialQueryRequest.getPageSize();
        Page<CourseMaterial> materialPage = materialService.page(new Page<>(current, size),
                materialService.getQueryWrapper(materialQueryRequest));
        return ApiResponse.success(materialService.getMaterialVOPage(materialPage));
    }

    // endregion

    // region 课程资料

    /**
     * 获取课程的资料列表
     */
    @GetMapping("/course/{courseId}")
    public ApiResponse getCourseMaterials(@PathVariable Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<CourseMaterialVO> materials = materialService.getCourseMaterials(courseId);
        return ApiResponse.success(materials);
    }

    /**
     * 下载资料（增加下载次数）
     */
    @PostMapping("/download/{id}")
    public ApiResponse downloadMaterial(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = materialService.downloadMaterial(id);
        return ApiResponse.success(result);
    }

    // endregion
}
