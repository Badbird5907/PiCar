package dev.badbird.picar.platform.impl;

import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.impl.noop.NoOpMotorController;
import dev.badbird.picar.platform.IPlatform;

public class NoOpPlatform implements IPlatform {
    private boolean ledState = false;
    private IMotorController motorController;
    @Override
    public void init() {
        motorController = new NoOpMotorController(this);

    }

    @Override
    public void cleanup() {

    }

    @Override
    public IMotorController getMotorController() {
        return motorController;
    }
}
