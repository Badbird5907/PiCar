package dev.badbird.picar.system.impl;

import dev.badbird.picar.motor.IMotorController;
import dev.badbird.picar.motor.impl.noop.NoOpMotorController;
import dev.badbird.picar.system.IPlatform;

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
    public void setLedState(boolean state) {
        System.out.println("LED state set to " + state);
        ledState = state;
    }

    @Override
    public boolean getLedState() {
        return ledState;
    }

    @Override
    public IMotorController getMotorController() {
        return motorController;
    }
}
