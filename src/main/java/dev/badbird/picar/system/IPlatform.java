package dev.badbird.picar.system;

import dev.badbird.picar.motor.IMotorController;

public interface IPlatform {
    void init();
    void cleanup();
    void setLedState(boolean state);
    boolean getLedState();
    IMotorController<?> getMotorController();
}
