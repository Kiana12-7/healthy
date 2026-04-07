package org.example.api.enums;

import lombok.Getter;

@Getter
public enum TrainLocation {
    CHEST((byte) 0, "胸部"),
    ARM_LEG((byte) 1, "臂腿"),
    CORE((byte) 2, "腰腹"),
    SHOULDER_ARM((byte) 3, "肩臂"),
    FULL_BODY((byte) 4, "全身");

    private final byte value;
    private final String description;

    TrainLocation(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TrainLocation fromByte(byte b) {
        for (TrainLocation location : values()) {
            if (location.value == b) {
                return location;
            }
        }
        throw new IllegalArgumentException("未知的TrainLocation值: " + b);
    }
}
