package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.CourseMaterial;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 课程资料表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-19
 */
public interface CourseMaterialMapper extends BaseMapper<CourseMaterial> {

    /**
     * 增加下载次数
     */
    @Update("UPDATE course_material SET download_count = download_count + 1 WHERE id = #{id}")
    Integer incrementDownloadCount(@Param("id") Long id);
}
