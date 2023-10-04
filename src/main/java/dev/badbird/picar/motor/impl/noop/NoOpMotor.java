package dev.badbird.picar.motor.impl.noop;

import dev.badbird.picar.motor.IMotor;
import dev.badbird.picar.motor.MotorSide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;

@AllArgsConstructor
public class NoOpMotor implements IMotor {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(NoOpMotor.class);
    @Getter
    private MotorSide side;
    @Override
    public void forward() {
        LOGGER.info("{} Forward", side.name());
    }

    @Override
    public void backward() {
        LOGGER.info("{} Backward", side.name());
    }

    @Override
    public void stop() {
        LOGGER.info("{} Stop", side.name());
    }
}
