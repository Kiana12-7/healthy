package org.example.api.enums;

import lombok.Getter;

@Getter
public enum BodyType {
    RECTANGLE((byte) 0, "直筒型"),
    PEAR((byte) 1, "梨型"),
    HOURGLASS((byte) 2, "沙漏型"),
    APPLE((byte) 3, "苹果型");

    private final byte value;
    private final String description;

    BodyType(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static BodyType fromByte(byte b) {
        for (BodyType type : values()) {
            if (type.value == b) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的BodyType值: " + b);
    }
}
