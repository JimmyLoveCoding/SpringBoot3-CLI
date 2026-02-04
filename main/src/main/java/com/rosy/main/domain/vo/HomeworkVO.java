package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class HomeworkVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long courseId;

    private String courseName;

    private String title;

    private String description;

    private String attachment;

    private LocalDateTime deadline;

    private Integer fullScore;

    private Byte status;

    private LocalDateTime createTime;
}
