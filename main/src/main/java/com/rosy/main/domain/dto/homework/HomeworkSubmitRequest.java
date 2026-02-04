package com.rosy.main.domain.dto.homework;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class HomeworkSubmitRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "作业ID不能为空")
    private Long homeworkId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    private String content;

    private String attachment;
}
