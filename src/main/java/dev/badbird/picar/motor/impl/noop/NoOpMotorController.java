package dev.badbird.picar.motor.impl.noop;

import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.MotorSide;
import dev.badbird.picar.platform.IPlatform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class NoOpMotorController implements IMotorController<NoOpMotor> {
    @Getter
    private Map<MotorSide, NoOpMotor> motors;
    private final IPlatform platform;

    @Override
    public IPlatform getPlatform() {
        return platform;
    }

    @Override
    public void init() {
        motors = new HashMap<>();
        motors.put(MotorSide.TOP_LEFT, new NoOpMotor(MotorSide.TOP_LEFT));
        motors.put(MotorSide.TOP_RIGHT, new NoOpMotor(MotorSide.TOP_RIGHT));
    }

    @Override
    public void cleanup() {

    }

    @Override
    public int getMotorCount() {
        return motors.size();
    }

    @Override
    public Optional<NoOpMotor> getMotor(MotorSide side) {
        return Optional.ofNullable(motors.get(side));
    }

}
