package org.example.api.enums;

import lombok.Getter;

@Getter
public enum BodyType {
    ECTOMORPH((byte) 0, "外胚型（瘦长）"),
    MESOMORPH((byte) 1, "中胚型（肌肉型）"),
    ENDOMORPH((byte) 2, "内胚型（易胖）");

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
