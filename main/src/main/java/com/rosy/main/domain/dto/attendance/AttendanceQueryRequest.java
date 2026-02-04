package com.rosy.main.domain.dto.attendance;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttendanceQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long courseId;

    private Long studentId;

    private LocalDate attendDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private Byte status;
}
