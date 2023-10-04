package dev.badbird.picar.motor;

import lombok.Getter;

public enum MotorSide {
    TOP_LEFT(Half.LEFT),
    TOP_RIGHT(Half.RIGHT),
    BOTTOM_LEFT(Half.LEFT),
    BOTTOM_RIGHT(Half.RIGHT);
    @Getter
    private final Half half;
    MotorSide(Half half) {
        this.half = half;
    }

    public enum Half {
        LEFT,
        RIGHT
    }
}
