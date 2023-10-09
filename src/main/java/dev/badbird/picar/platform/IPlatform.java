package dev.badbird.picar.platform;

import dev.badbird.picar.motor.IMotorController;

public interface IPlatform {
    void init();
    void cleanup();
    IMotorController<?> getMotorController();
}
