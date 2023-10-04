package dev.badbird.picar.motor;

public interface IMotor {
    void forward();

    void backward();

    void stop();

    MotorSide getSide();
}
