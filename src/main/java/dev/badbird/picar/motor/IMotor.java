package dev.badbird.picar.motor;

import dev.badbird.picar.object.MotorState;

public interface IMotor {
    void forward();

    void backward();

    void stop();

    void setSpeed(int speed);

    MotorSide getSide();

    MotorState getState();
}
