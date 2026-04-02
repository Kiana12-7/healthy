package org.example.api.enums;

import lombok.Getter;

@Getter
public enum TrainLocation {
    HOME((byte) 0, "居家"),
    GYM((byte) 1, "健身房"),
    OUTDOOR((byte) 2, "户外"),
    MIXED((byte) 3, "混合");

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
