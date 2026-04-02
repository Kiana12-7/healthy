package org.example.api.enums;

import lombok.Getter;

@Getter
public enum GoalState {
    LOSE_FAT((byte) 0, "减脂"),
    BUILD_MUSCLE((byte) 1, "增肌"),
    KEEP_FIT((byte) 2, "保持健康"),
    IMPROVE_ENDURANCE((byte) 3, "提升耐力");

    private final byte value;
    private final String description;

    GoalState(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static GoalState fromByte(byte b) {
        for (GoalState state : values()) {
            if (state.value == b) {
                return state;
            }
        }
        throw new IllegalArgumentException("未知的GoalState值: " + b);
    }
}
