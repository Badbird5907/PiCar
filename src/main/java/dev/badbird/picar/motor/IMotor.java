package dev.badbird.picar.motor;

import dev.badbird.picar.object.MotorState;

public interface IMotor {
    void forward();

    void backward();

    void stop();

    MotorSide getSide();

    MotorState getState();
}
