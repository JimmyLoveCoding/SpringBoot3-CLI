package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("course")
public class Course implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String courseCode;

    private String courseName;

    private BigDecimal credits;

    private Integer hours;

    private Long teacherId;

    private String classroom;

    private String semester;

    private Byte weekday;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer maxStudents;

    private String description;

    private Byte status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Byte isDeleted;
}
