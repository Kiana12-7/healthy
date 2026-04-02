package org.example.api.enums;

import lombok.Getter;

@Getter
public enum CurrentState {
    DRAFT((byte) 0, "草稿"),
    SUBMITTED((byte) 1, "已提交"),
    PROCESSING((byte) 2, "处理中"),
    COMPLETED((byte) 3, "已完成");

    private final byte value;
    private final String description;

    CurrentState(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CurrentState fromByte(byte b) {
        for (CurrentState state : values()) {
            if (state.value == b) {
                return state;
            }
        }
        throw new IllegalArgumentException("未知的CurrentState值: " + b);
    }
}
