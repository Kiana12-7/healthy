package org.example.api.enums;

import lombok.Getter;

@Getter
public enum HurtLocation {
    NONE((byte) 0, "无受伤"),
    SHOULDER((byte) 1, "肩部"),
    KNEE((byte) 2, "膝盖"),
    BACK((byte) 3, "背部"),
    ELBOW((byte) 4, "肘部"),
    WRIST((byte) 5, "手腕"),
    ANKLE((byte) 6, "脚踝");

    private final byte value;
    private final String description;

    HurtLocation(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static HurtLocation fromByte(byte b) {
        for (HurtLocation location : values()) {
            if (location.value == b) {
                return location;
            }
        }
        throw new IllegalArgumentException("未知的HurtLocation值: " + b);
    }
}
