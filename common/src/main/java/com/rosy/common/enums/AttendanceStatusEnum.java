package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum AttendanceStatusEnum {

    PRESENT(0, "出勤"),
    ABSENT(1, "缺勤"),
    LATE(2, "迟到"),
    LEAVE(3, "请假");

    private final int code;
    private final String description;

    AttendanceStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AttendanceStatusEnum getByCode(int code) {
        for (AttendanceStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
