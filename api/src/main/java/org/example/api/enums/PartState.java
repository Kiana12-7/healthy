package org.example.api.enums;

import lombok.Getter;

@Getter
public enum PartState {
    FLABBY((byte) 0, "松弛赘肉"),
    SHRIVELED((byte) 1, "干瘪"),
    AVERAGE((byte) 2, "匀称"),
    TONED((byte) 3, "线条紧致"),
    DEFINED((byte) 4, "腹肌清晰");

    private final byte value;
    private final String description;

    PartState(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static PartState fromByte(byte b) {
        for (PartState state : values()) {
            if (state.value == b) {
                return state;
            }
        }
        throw new IllegalArgumentException("未知的CurrentState值: " + b);
    }
}
