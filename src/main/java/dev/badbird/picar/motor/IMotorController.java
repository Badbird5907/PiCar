package dev.badbird.picar.motor;

import dev.badbird.picar.object.MotorMovementState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IMotorController<T extends IMotor> {
    void init();

    void cleanup();

    default void startForward() {
        getMotors().values().forEach(IMotor::forward);
    }

    default void startBackward() {
        getMotors().values().forEach(IMotor::backward);
    }

    default void stop() {
        getMotors().values().forEach(IMotor::stop);
    }

    int getMotorCount();

    Optional<T> getMotor(MotorSide side);
    Map<MotorSide, T> getMotors();
    default List<T> getHalf(MotorSide.Half half) {
        return getMotors().values().stream().filter(motor -> motor.getSide().getHalf() == half).toList();
    }

    default void startLeft() {
        getHalf(MotorSide.Half.LEFT).forEach(IMotor::backward);
        getHalf(MotorSide.Half.RIGHT).forEach(IMotor::forward);
    }

    default void startRight() {
        getHalf(MotorSide.Half.LEFT).forEach(IMotor::forward);
        getHalf(MotorSide.Half.RIGHT).forEach(IMotor::backward);
    }

    void speed(double speed);

    double getSpeed();

    default double speed() {
        return getSpeed();
    }

    default void setSpeed(double speed) {
        speed(getSpeed());
    }

    default Map<MotorSide, MotorMovementState> getMovementStates() {
        Map<MotorSide, MotorMovementState> map = new HashMap<>();
        getMotors().forEach((side, motor) -> map.put(side, motor.getState().getMovementState()));
        return map;
    }
}

