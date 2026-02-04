package com.rosy.main.domain.dto.material;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 资料查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialQueryRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 资料标题
     */
    private String title;

    /**
     * 文件类型
     */
    private String fileType;
}
