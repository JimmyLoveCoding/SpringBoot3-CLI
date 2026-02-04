package com.rosy.main.domain.dto.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 资料添加请求
 */
@Data
public class MaterialAddRequest {

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 资料标题
     */
    @NotBlank(message = "资料标题不能为空")
    private String title;

    /**
     * 资料描述
     */
    private String description;

    /**
     * 文件URL
     */
    @NotBlank(message = "文件URL不能为空")
    private String fileUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;
}
