package com.rosy.main.domain.dto.homework;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class HomeworkAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "作业标题不能为空")
    private String title;

    private String description;

    private String attachment;

    private LocalDateTime deadline;

    private Integer fullScore;
}
