package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum LeaveStatusEnum {

    PENDING(0, "待审批"),
    APPROVED(1, "已批准"),
    REJECTED(2, "已拒绝");

    private final int code;
    private final String description;

    LeaveStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LeaveStatusEnum getByCode(int code) {
        for (LeaveStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
