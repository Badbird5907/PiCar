package dev.badbird.picar.object;

import dev.badbird.picar.motor.MotorSide;
import lombok.Data;

@Data
public class MotorState {
    private final MotorSide side;
    private final MotorMovementState movementState;
    private final int speed;
}
