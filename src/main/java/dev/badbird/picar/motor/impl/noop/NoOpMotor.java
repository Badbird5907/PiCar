package dev.badbird.picar.motor.impl.noop;

import dev.badbird.picar.motor.IMotor;
import dev.badbird.picar.motor.MotorSide;
import dev.badbird.picar.object.MotorMovementState;
import dev.badbird.picar.object.MotorState;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;

@Data
public class NoOpMotor implements IMotor {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(NoOpMotor.class);
    @Getter
    private final MotorSide side;
    private MotorMovementState state = MotorMovementState.STOPPED;
    private int speed = 100;

    @Override
    public void forward() {
        LOGGER.info("{} Forward", side.name());
        state = MotorMovementState.FORWARD;
    }

    @Override
    public void backward() {
        LOGGER.info("{} Backward", side.name());
        state = MotorMovementState.BACKWARD;
    }

    @Override
    public void stop() {
        LOGGER.info("{} Stop", side.name());
        state = MotorMovementState.STOPPED;
    }

    @Override
    public MotorState getState() {
        return new MotorState(
                side,
                state,
                speed
        );
    }
}
