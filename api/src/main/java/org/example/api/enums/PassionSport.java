package org.example.api.enums;

import lombok.Getter;

@Getter
public enum PassionSport {
    RUNNING((byte) 0, "跑步"),
    SWIMMING((byte) 1, "游泳"),
    WEIGHT_TRAINING((byte) 2, "力量训练"),
    YOGA((byte) 3, "瑜伽"),
    CYCLING((byte) 4, "骑行"),
    BASKETBALL((byte) 5, "篮球"),
    FOOTBALL((byte) 6, "足球");

    private final byte value;
    private final String description;

    PassionSport(byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public static PassionSport fromByte(byte b) {
        for (PassionSport sport : values()) {
            if (sport.value == b) {
                return sport;
            }
        }
        throw new IllegalArgumentException("未知的PassionSport值: " + b);
    }
}
